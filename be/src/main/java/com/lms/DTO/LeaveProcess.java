package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveProcess extends BaseEmail {

	private Integer approvalId;

	private	ApprovalStatus status;

	private UserDTO processBy;
}
