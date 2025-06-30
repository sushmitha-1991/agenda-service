package com.dsp.agendaPapers.eSummonNotification.repository;



import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

    // new code for mail
    @Query("SELECT e.memberId FROM ESummonMembers e WHERE e.eSummonId = :eSummonId")
    List<Long> findMemberIdsByESummonId(@Param("eSummonId") Long eSummonId);


    // ✅ English Display Name
    @Query(value = """
        SELECT
            CONCAT_WS(' ',
                CASE
                    WHEN c.abbr = 'NAME_INITIAL_Other' THEN pd.initials_other_en
                    ELSE c.code
                END,
                NULLIF(TRIM(pd.first_name), ''),
                NULLIF(TRIM(pd.middle_name), ''),
                NULLIF(TRIM(pd.last_name), '')
            ) AS displayNameEn
        FROM members.personal_details pd
        LEFT JOIN master.codes c ON c.id = pd.initials_id
        WHERE pd.id = :memberId
        """, nativeQuery = true)
    String getDisplayNameEn(@Param("memberId") Long memberId);

    // ✅ Hindi Display Name
    @Query(value = """
        SELECT
            CONCAT_WS(' ',
                CASE
                    WHEN c.abbr = 'NAME_INITIAL_Other' THEN pd.initials_other_hi
                    ELSE c.code_data->>'hi'
                END,
                NULLIF(TRIM(pd.first_name_hindi), ''),
                NULLIF(TRIM(pd.middle_name_hi), ''),
                NULLIF(TRIM(pd.last_name_hindi), '')
            ) AS displayNameHi
        FROM members.personal_details pd
        LEFT JOIN master.codes c ON c.id = pd.initials_id
        WHERE pd.id = :memberId
        """, nativeQuery = true)
    String getDisplayNameHi(@Param("memberId") Long memberId);
}

