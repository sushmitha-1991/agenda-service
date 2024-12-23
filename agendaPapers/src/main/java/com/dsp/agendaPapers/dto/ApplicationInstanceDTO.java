package com.dsp.agendaPapers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInstanceDTO {

      private Long id;
      private  Long   applicationId;
      private String  applicationNo;
      private   String  currentStatus;
      private Long refId;
      // adding the date
      private LocalDate documentDate;
}
