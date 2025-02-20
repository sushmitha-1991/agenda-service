	package com.dsp.agendaPapers.eSummonNotification.dto;

	import java.time.LocalDate;


	import java.time.LocalDateTime;
	import java.util.List;

	import com.dsp.agendaPapers.provisionalcalendarfinal.dto.InputInstanceDTO;
	import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
	import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;

	import lombok.AllArgsConstructor;
	import lombok.Builder;
	import lombok.Data;
	import lombok.NoArgsConstructor;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public class ESummonNotificationDTO {

		private Long id;

		private String lokSabhaNo;

		private String sessionNo;

		private String rajyasabhaSessionNo;

		private LocalDate esummonIssuanceDate;

		private String esummonIssuanceDateHindiEn;

		private String esummonIssuanceDateHindi;

		private Boolean draftStatus = false;

		private LocalDateTime esummonStartAt;

		//private LocalDateTime summonStartAt;

		private Long applicationId;

		private String applicationNo;

		private String status;


		private SummonNotificationDTO parentNotification;

		private String templateContent;
		private Boolean customTemplate;
		private ApplicationInstanceDTO applicationInstanceDTO;

		private InputInstanceDTO inputInstanceDTO;

		private String currentStatus;

		private String publishedDocumentUrl;

		private Long templateId;

		private Long templateHindiId;

		private String templateContentHindi;

		private String templateType;

		private Long refId;


		private List<MemberIdDTO> memberIds; // Added new field

		// Inner DTO class for member IDs
		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class MemberIdDTO {
			private Long id;
		}






	}
