package com.lms.models;

import com.lms.dto.ConfigurationInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, name = "milestone_year")
	private Integer milestoneYear;

	@Column(nullable = false, name = "limit_attachment")
	private Integer limitAttachment;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, name = "created_date")
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(nullable = false, name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "updated_by")
	private String updatedBy;

	public Configuration(Long id, Integer milestoneYear, Integer limitAttachment, String updatedBy) {
		this.id = id;
		this.milestoneYear = milestoneYear;
		this.limitAttachment = limitAttachment;
		this.updatedBy = updatedBy;
	}

	public ConfigurationInfo toConfigurationInfo() {
		return new ConfigurationInfo(this.id, this.milestoneYear, this.limitAttachment, this.updatedBy);
	}

}
