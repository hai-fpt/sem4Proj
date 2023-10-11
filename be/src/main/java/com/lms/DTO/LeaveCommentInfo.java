package com.lms.dto;

import lombok.Data;

@Data
public class LeaveCommentInfo {

	private Long id;
	private String comment;
	private String author;
	private Long leaveRequestId;
	private String updatedBy;

	public LeaveCommentInfo(Long id, String comment, String author, Long leaveRequestId, String updatedBy) {
		this.id = id;
		this.comment = comment;
		this.author = author;
		this.leaveRequestId = leaveRequestId;
		this.updatedBy = updatedBy;
	}
}
