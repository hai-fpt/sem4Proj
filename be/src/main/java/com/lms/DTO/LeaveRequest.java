package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest extends BaseEmail {

	private User requester;

	private String fromDate;

	private String toDate;

	private String reason;
}
