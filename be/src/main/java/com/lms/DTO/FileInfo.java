package com.lms.dto;

import lombok.Data;

@Data
public class FileInfo {
	private String name;
	private String path;
	private String updatedBy;

	public FileInfo(String name, String path, String updatedBy) {
		this.name = name;
		this.path = path;
		this.updatedBy = updatedBy;
	}
}
