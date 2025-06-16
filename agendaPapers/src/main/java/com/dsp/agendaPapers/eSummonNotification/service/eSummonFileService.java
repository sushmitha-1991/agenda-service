package com.dsp.agendaPapers.eSummonNotification.service;


import com.dsp.agendaPapers.eSummonNotification.dto.eSummonFileDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class eSummonFileService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Value("${openKm.dsp1.download.endpoint}")
    private String baseUrl;

    public List<eSummonFileDto> getFilesListByMpCode(String mpCode) {
        String sql = """
            SELECT 
                esh.document_uuid AS english_document,
                esh.document_uuid_hindi AS hindi_document
            FROM 
                agenda_papers.e_summon es
            LEFT JOIN 
                agenda_papers.e_summon_members esm ON esm.e_summon_id = es.id
            LEFT JOIN 
                members.personal_details pd ON pd.id = esm.member_id
            LEFT JOIN 
                agenda_papers.e_summon_history esh ON esh.e_summon_id = es.id AND esh.member_id = pd.id
            WHERE 
                pd.mp_code = :mpCode
        """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpCode", mpCode);


        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            List<eSummonFileDto> dtoList = new ArrayList<>();

            String english = rs.getString("english_document");
            String hindi = rs.getString("hindi_document");

            if (english != null && !english.isEmpty()) {
                dtoList.add(eSummonFileDto.builder()
                        .fileUrl(baseUrl + english)
                        .build());
            }

            if (hindi != null && !hindi.isEmpty()) {
                dtoList.add(eSummonFileDto.builder()
                        .fileUrl(baseUrl + hindi)
                        .build());
            }

            return dtoList;
        }).stream().flatMap(List::stream).toList(); // Flatten the list
    }
}