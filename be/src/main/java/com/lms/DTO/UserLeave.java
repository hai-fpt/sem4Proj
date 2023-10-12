package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.dto.Leave;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLeave {
	private Long id;
	private User user;
	private Leave leave;
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime fromDate;
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime toDate;
	private ApprovalStatus status;
	private String reason;
	private String updatedBy;
	private List<Long> informTo;
	private List<Long> teamLeads;
	private MultipartFile[] attachments;

}
