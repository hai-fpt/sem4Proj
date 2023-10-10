package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestEmail implements Serializable {

	private Integer requestId;

	private List<String> sendTos;

	private List<String> ccTos;

	private List<String> dearTos;

	private String subject = "[VDC] Leave Request";

	private UserDTO requester;

	private String fromDate;

	private String toDate;

	private String reason;
}
