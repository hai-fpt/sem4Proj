package com.lms.service;

import com.lms.dto.LeaveProcess;
import com.lms.dto.LeaveRequest;
import com.lms.exception.InvalidReceiverException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl extends BaseSendMail implements EmailService {

	public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
		super(mailSender, springTemplateEngine);
	}

	@Override
	public void sendRequest(LeaveRequest leaveRequest) throws InvalidReceiverException, MessagingException {
		List<String> sendTos = leaveRequest.getSendTos();
		if (sendTos.isEmpty()) {
			throw new InvalidReceiverException("Receiver must not be null or empty");
		}

		Map<String, Object> properties = new HashMap<>();
		properties.put("reason", leaveRequest.getReason());
		properties.put("fromDate", leaveRequest.getFromDate());
		properties.put("toDate", leaveRequest.getToDate());
		properties.put("team", leaveRequest.getRequester().getTeam());
		properties.put("sendBy", leaveRequest.getRequester().getName());
		properties.put("dears", leaveRequest.getDearTos());
		properties.put("link", leaveRequest.getLink());
		List<String> ccTos = leaveRequest.getCcTos();
		sendEmail(sendTos.toArray(new String[sendTos.size()]), ccTos.toArray(new String[ccTos.size()]), leaveRequest.getSubject(), "leave_request.html", properties);
	}

	@Override
	public void sendApproval(LeaveProcess process) throws MessagingException {
		List<String> sendTos = process.getSendTos();
		Map<String, Object> properties = new HashMap<>();
		properties.put("dears", process.getDearTos());
		properties.put("status", process.getStatus().name());
		properties.put("processBy", process.getProcessBy().getName());
		properties.put("link", process.getLink());
		String subject = process.getSubject().isEmpty() ? "[VDC] Leave request process" : process.getSubject();
		List<String> ccTos = process.getCcTos();
		sendEmail(sendTos.toArray(new String[sendTos.size()]), ccTos.toArray(new String[ccTos.size()]), subject, "leave_approval.html", properties);
	}
}
