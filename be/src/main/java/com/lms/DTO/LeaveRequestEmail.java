package com.lms.DTO;

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

	private String subject;

	private String sendBy;

	private String team;

	private String fromDate;

	private String toDate;

	private String reason;

}
