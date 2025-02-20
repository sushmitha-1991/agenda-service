package com.dsp.agendaPapers.eSummonHistory.entity;


import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "members", name = "personal_details")
public class PersonalDetails {

    @Id
    private Long id;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "ic_no", length = 10)
    private String icNo;

    @Column(name = "email_id", length = 255)
    private String emailId;


}