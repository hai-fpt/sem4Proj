package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveProcess extends BaseEmail {

	private Integer approvalId;

	private	ApprovalStatus status;
	private String rejectedReason;

	private List<User> processBys = new ArrayList<>();

	public void setProcessBy(User user) {
		processBys.add(user);
	}

	public String[] getProcessBysAsArray() {
		return processBys.stream().map(User::getName).toArray(String[]::new);
	}
}
