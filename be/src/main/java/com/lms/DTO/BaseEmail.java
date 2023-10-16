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
public class BaseEmail {

	private String leaveRequestId;

	private List<String> sendTos = new ArrayList<>();

	private List<String> ccTos = new ArrayList<>();

	private List<String> dearTos = new ArrayList<>();

	private String link;

	private String subject;
}
