package com.lms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_storage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileStorage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, name = "name")
	private String name;

	@Column(nullable = false, name = "path")
	private String path;

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

	public FileStorage(String name, String path, UserLeave leaveRequest, String updatedBy) {
		this.name = name;
		this.path = path;
		this.leaveRequest = leaveRequest;
		this.updatedBy = updatedBy;
	}
}
