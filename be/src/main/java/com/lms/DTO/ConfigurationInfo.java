package com.lms.dto;

import lombok.Data;

@Data
public class ConfigurationInfo {

	private Long id;
	private Integer milestoneYear;
	private Integer limitAttachment;
	private String updatedBy;

	public ConfigurationInfo(Long id, Integer milestoneYear, Integer limitAttachment, String updatedBy) {
		this.id = id;
		this.milestoneYear = milestoneYear;
		this.limitAttachment = limitAttachment;
		this.updatedBy = updatedBy;
	}
}
