package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.models.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Data
@NoArgsConstructor
public class NotificationInfo {

	Long id;

	private boolean type;

	private Long senderId;

	private String senderName;

	private String senderEmail;

	private Long receiverId;

	private String receiverName;

	private String receiverEmail;

	private ApprovalStatus status;

	private boolean isWatched;

	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime leaveFrom;

	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime leaveTo;

	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime createdDate;

	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime watchedDate;

	private String message;

	public Notification toNotification(){
		return new Notification(id, type, senderId, senderName, senderEmail, receiverId, receiverName, receiverEmail,
				status, isWatched, leaveFrom, leaveTo, createdDate, watchedDate);
	}

}
