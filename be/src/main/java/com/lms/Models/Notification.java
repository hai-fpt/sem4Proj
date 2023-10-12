package com.lms.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.dto.ApprovalStatus;
import com.lms.dto.NotificationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, name = "type")
	private boolean type;

	@Column(nullable = false, name = "sender_id")
	private Long senderId;

	@Column(nullable = false, name = "sender_name")
	private String senderName;

	@Column(nullable = false, name = "sender_email")
	private String senderEmail;

	@Column(nullable = false, name = "receiver_id")
	private Long receiverId;

	@Column(nullable = false, name = "receiver_name")
	private String receiverName;

	@Column(nullable = false, name = "receiver_email")
	private String receiverEmail;

	@Column(nullable = false, name = "status")
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;

	@Column(nullable = false, name = "is_watched")
	private boolean isWatched;

	@CreationTimestamp
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	@Column(nullable = false, updatable = false, name = "leave_from")
	private LocalDateTime leaveFrom;


	@Column(nullable = false, updatable = false, name = "leave_to")
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	private LocalDateTime leaveTo;

	@CreationTimestamp
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	@Column(nullable = false, updatable = false, name = "created_date")
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
	@Column(nullable = false, updatable = false, name = "watched_date")
	private LocalDateTime watchedDate;


	public NotificationInfo toNotificationInfo() {
		NotificationInfo responseNotification = new NotificationInfo();
		responseNotification.setId(id);
		responseNotification.setType(type);
		responseNotification.setStatus(status);
		responseNotification.setLeaveFrom(leaveFrom);
		responseNotification.setLeaveTo(leaveTo);
		responseNotification.setReceiverId(receiverId);
		responseNotification.setReceiverName(receiverName);
		responseNotification.setReceiverEmail(receiverEmail);
		responseNotification.setCreatedDate(createdDate);
		responseNotification.setWatched(isWatched);
		responseNotification.setWatchedDate(watchedDate);
		StringBuilder message = new StringBuilder();
		//true means response ( in case leader approve or reject request)
		if (type) {
			responseNotification.setSenderName(senderName);
			responseNotification.setSenderId(senderId);
			responseNotification.setSenderEmail(senderEmail);
			message.append(senderName).append(" (").append(senderEmail).append(")");
			switch (status) {
				case REJECTED:
					message.append(" reject your leave request ");
					break;
				case APPROVED:
					message.append(" approved your leave request ");
			}
			//false means request (in cases: a new request / a cancel request / finally status(APPROVED-REJECTED)
		} else {
			switch (status) {
				case PENDING:
					responseNotification.setSenderName(senderName);
					responseNotification.setSenderId(senderId);
					responseNotification.setSenderEmail(senderEmail);
					message.append(senderName).append(" (").append(senderEmail).append(")");
					message.append(" send a leave request from: ")
							.append(leaveFrom.format(DateTimeFormatter.ofPattern(JSON_VIEW_DATE_FORMAT)))
							.append(" to: ").append(leaveFrom.format(DateTimeFormatter.ofPattern(JSON_VIEW_DATE_FORMAT)));
					break;
				case CANCELLED:
					responseNotification.setSenderName(senderName);
					responseNotification.setSenderId(senderId);
					responseNotification.setSenderEmail(senderEmail);
					message.append(senderName).append(" (").append(senderEmail).append(")");
					message.append(" has canceled leave request");
					break;
				case APPROVED:
					message.append("Your leave request has been finished with final status: APPROVED!");
					break;
				case REJECTED:
					message.append("Your leave request has been finished with final status: REJECTED!");
			}
		}
		responseNotification.setMessage(message.toString());
		return responseNotification;
	}
}
