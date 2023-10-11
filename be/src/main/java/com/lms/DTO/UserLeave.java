package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.models.Leave;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLeave {
	private Long id;
	private User user;
	//TODO: change to DTO after merge
	private Leave leave;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime fromDate;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime toDate;
	private ApprovalStatus status;
	private String reason;
	private String requestedByEmail;
	private List<Long> informTo;
	private List<Long> teamLeads;
	private MultipartFile[] attachments;

}
