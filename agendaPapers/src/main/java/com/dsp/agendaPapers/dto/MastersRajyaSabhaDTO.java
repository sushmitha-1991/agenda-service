
package com.dsp.agendaPapers.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MastersRajyaSabhaDTO {
    @NotEmpty
    private Long id;

    @NotEmpty
    private String sessionNumber;
    
	}

    
    



