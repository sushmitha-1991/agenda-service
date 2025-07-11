package com.dsp.agendaPapers.eSummonNotification.service;


import com.dsp.agendaPapers.eSummonNotification.dto.SummonResponseRSDTO;
import com.dsp.agendaPapers.eSummonNotification.dto.eSummonFileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class eSummonFileService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Value("${openKm.dsp1.download.endpoint}")
    private String baseUrl;
    
    @Autowired
    private EntityManager entityManager;

    public List<eSummonFileDto> getFilesListByMpCode(String mpCode, Long houseId) {
        String sql = """
             SELECT 
			    esh.document_uuid AS english_document,
			    esh.document_uuid_hindi AS hindi_document
			FROM agenda_papers.e_summon_history esh
			JOIN members.personal_details pd ON pd.id = esh.member_id
			            WHERE  
                pd.mp_code = :mpCode
                AND pd.house_id=:houseId
                AND esh.is_active = true
				  AND esh.is_deleted = false
				  and esh.document_uuid is not null
				ORDER BY esh.created_at DESC
				LIMIT 1
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpCode", mpCode);
        params.addValue("houseId", houseId);


        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            List<eSummonFileDto> dtoList = new ArrayList<>();

            String english = rs.getString("english_document");
            String hindi = rs.getString("hindi_document");

            if (english != null && !english.isEmpty()) {
                dtoList.add(eSummonFileDto.builder()
                        .fileUrl(baseUrl + english)
                        .fileName("ESummon English")
                        .build());
            }

            if (hindi != null && !hindi.isEmpty()) {
                dtoList.add(eSummonFileDto.builder()
                        .fileUrl(baseUrl + hindi)
                        .fileName("ESummon Hindi")
                        .build());
            }

            return dtoList;
        }).stream().flatMap(List::stream).toList();
    }
    public SummonResponseRSDTO getRsFilesListByMpCode(String mpCode) {
        String sql = """
             SELECT 
                rm.session_number AS sessionNoEng,
                rm.session_number AS sessionNoHin,
                TO_CHAR(es.e_summon_issuance_date, 'DD/MM/YYYY') AS summonDateEng,
                es.e_summon_issuance_date_hindi_en AS summonDateTextEng,
                es.template_content AS textEnglish,
                es.template_content_hindi AS textHindi,
                'P.C. MODY,' AS sgNameEng,
                'पी. सी. मोदी,' AS sgNameHin,
                CONCAT_WS(' ', pd.first_name, pd.last_name) AS mpFullNameEng,
                CONCAT_WS(' ', pd.first_name_hindi, pd.last_name_hindi) AS mpFullNameHin,
                es.e_summon_issuance_date_hindi AS summonDateTextHin,
                es.document_list
            FROM agenda_papers.e_summon es
            JOIN master.rajya_sabha_session_master rm ON rm.session_number = es.rajya_sabha_session_no
            JOIN agenda_papers.e_summon_history h ON h.e_summon_id = es.id
            JOIN members.personal_details pd ON pd.id = h.member_id
            WHERE pd.mp_code = :mpCode and es.e_summon_issuance_date_hindi_en is not null
            ORDER BY es.e_summon_issuance_date DESC
            LIMIT 1
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mpCode", mpCode);

        List<Object[]> results = query.getResultList();
        if (results.isEmpty()) return null;

        Object[] row = results.get(0);

        SummonResponseRSDTO dto = new SummonResponseRSDTO();
        try {
            int sessionNumber = Integer.parseInt(row[0].toString());
            dto.setSessionNoEng(convertToWords(sessionNumber).toUpperCase() + " SESSION");
        } catch (Exception e) {
            dto.setSessionNoEng(safeString(row[0]) + " SESSION");
        }

        try {
            int sessionNumber = Integer.parseInt(row[1].toString());
            dto.setSessionNoHin(getHindiOrdinal(sessionNumber) + " सत्र");
        } catch (Exception e) {
            dto.setSessionNoHin(safeString(row[1]) + " सत्र");
        }

        dto.setSummonDateEng(safeString(row[2]));
        dto.setSummonDateTextEng(safeString(row[3]));
        dto.setTextEnglish(safeString(row[4]));
        dto.setTextHindi(safeString(row[5]));
        dto.setSgNameEng(safeString(row[6]));
        dto.setSgNameHin(safeString(row[7]));
        dto.setMpFullNameEng(safeString(row[8]));
        dto.setMpFullNameHin(safeString(row[9]));
        dto.setSummonDateTextHin(safeString(row[10]));

        String docListJson = safeString(row[11]);
        dto.setFiles(parseFileList(docListJson));

        return dto;
    }
    private String toWords(Object val) {
        try {
            int number = Integer.parseInt(val.toString());
            // Use Apache Commons or custom converter
            return convertToWords(number).toUpperCase() + " SESSION";
        } catch (Exception e) {
            return val.toString();
        }
    }

    // Hindi ordinal like "दो सौ सड़सठवां सत्र"
    private String toHindiOrdinal(Object val) {
        try {
            int number = Integer.parseInt(val.toString());
            return getHindiOrdinal(number) + " सत्र";
        } catch (Exception e) {
            return val.toString();
        }
    }

    private List<SummonResponseRSDTO.FileDTO> parseFileList(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            List<SummonResponseRSDTO.FileDTO> list = new ArrayList<>();

            for (JsonNode file : node) {
                SummonResponseRSDTO.FileDTO fileDTO = new SummonResponseRSDTO.FileDTO();
                fileDTO.setTitle(file.get("title").asText());
                fileDTO.setUrl(file.get("url").asText());
                list.add(fileDTO);
            }
            return list;
        } catch (Exception e) {
            return List.of(); // or return null
        }
    }
    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
            "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen"
        };

        private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty",
            "Sixty", "Seventy", "Eighty", "Ninety"
        };

        public static String convertToWords(int number) {
            if (number == 0) return "Zero";
            if (number < 0) return "Minus " + convertToWords(-number);

            StringBuilder words = new StringBuilder();

            if (number / 1000000 > 0) {
                words.append(convertToWords(number / 1000000)).append(" Million ");
                number %= 1000000;
            }

            if (number / 1000 > 0) {
                words.append(convertToWords(number / 1000)).append(" Thousand ");
                number %= 1000;
            }

            if (number / 100 > 0) {
                words.append(convertToWords(number / 100)).append(" Hundred ");
                number %= 100;
            }

            if (number > 0) {
                if (!words.toString().isEmpty()) words.append("and ");
                if (number < 20) words.append(units[number]);
                else {
                    words.append(tens[number / 10]);
                    if (number % 10 > 0) {
                        words.append(" ").append(units[number % 10]);
                    }
                }
            }

            return words.toString().trim();
        }

        // ---- Hindi Ordinal with Loop-Based Conversion ----

        private static final Map<Integer, String> hindiMap = new HashMap<>();

        static {
            String[] hindi = {
                "", "एक", "दो", "तीन", "चार", "पांच", "छह", "सात", "आठ", "नौ",
                "दस", "ग्यारह", "बारह", "तेरह", "चौदह", "पंद्रह", "सोलह", "सत्रह", "अठारह", "उन्नीस",
                "बीस", "इक्कीस", "बाईस", "तेईस", "चौबीस", "पच्चीस", "छब्बीस", "सत्ताईस", "अट्ठाईस", "उनतीस",
                "तीस", "इकतीस", "बत्तीस", "तैंतीस", "चौंतीस", "पैंतीस", "छत्तीस", "सैंतीस", "अड़तीस", "उनतालीस",
                "चालीस", "इकतालीस", "बयालीस", "तैंतालीस", "चवालीस", "पैंतालीस", "छियालीस", "सैंतालीस", "अड़तालीस", "उनचास",
                "पचास", "इक्यावन", "बावन", "तिरेपन", "चौवन", "पचपन", "छप्पन", "सत्तावन", "अट्ठावन", "उनसठ",
                "साठ", "इकसठ", "बासठ", "तिरेसठ", "चौंसठ", "पैंसठ", "छियासठ", "सड़सठ", "अड़सठ", "उनहत्तर",
                "सत्तर", "इकहत्तर", "बहत्तर", "तिहत्तर", "चौहत्तर", "पचहत्तर", "छिहत्तर", "सतहत्तर", "अठहत्तर", "उन्यासी",
                "अस्सी", "इक्यासी", "बयासी", "तिरासी", "चौरासी", "पचासी", "छियासी", "सत्तासी", "अठासी", "नवासी",
                "नब्बे", "इक्यानवे", "बानवे", "तिरानवे", "चौरानवे", "पंचानवे", "छियानवे", "सत्तानवे", "अट्ठानवे", "निन्यानवे"
            };

            for (int i = 1; i < hindi.length; i++) {
                hindiMap.put(i, hindi[i]);
            }

            hindiMap.put(100, "सौ");
            hindiMap.put(200, "दो सौ");
            hindiMap.put(300, "तीन सौ");
            // Extend as needed...
        }

        public static String getHindiOrdinal(int number) {
            StringBuilder result = new StringBuilder();

            if (number >= 100) {
                int hundreds = number / 100;
                if (hundreds == 1) result.append("एक सौ ");
                else result.append(hindiMap.getOrDefault(hundreds, String.valueOf(hundreds))).append(" सौ ");
                number %= 100;
            }

            if (number > 0) {
                result.append(hindiMap.getOrDefault(number, String.valueOf(number)));
            }

            return result.append("वां").toString().trim();
        }
        private String safeString(Object obj) {
            return obj == null ? "" : obj.toString().trim();
        }

}