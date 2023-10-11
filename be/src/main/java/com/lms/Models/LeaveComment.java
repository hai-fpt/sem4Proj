package com.lms.models;

import com.lms.dto.LeaveCommentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, name = "comment")
	private String comment;

	@Column(nullable = false, name = "author")
	private String author;

	@ManyToOne
	@JoinColumn(name = "request_id")
	private UserLeave leaveRequest;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, name = "created_date")
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(nullable = false, name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "updated_by")
	private String updatedBy;

	public LeaveComment(Long id, String comment, String author, UserLeave leaveRequest, String updatedBy) {
		this.id = id;
		this.comment = comment;
		this.author = author;
		this.leaveRequest = leaveRequest;
		this.updatedBy = updatedBy;
	}

	public LeaveCommentInfo toLeaveCommentInfo() {
		return new LeaveCommentInfo(this.id, this.comment, this.author, this.leaveRequest.getId(), this.updatedBy);
	}
}
