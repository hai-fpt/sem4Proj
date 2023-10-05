package com.lms.Service;

import com.lms.DTO.LeaveRequestEmail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {


	private JavaMailSender mailSender;
	private SpringTemplateEngine templateEngine;

	public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = springTemplateEngine;
	}

	@Override
	public void send(LeaveRequestEmail leaveRequest) throws MessagingException {
		Map<String, Object> properties = new HashMap<>();
		properties.put("reason", leaveRequest.getReason());
		properties.put("fromDate", leaveRequest.getFromDate());
		properties.put("toDate", leaveRequest.getToDate());
		properties.put("team", leaveRequest.getTeam());
		properties.put("sendBy", leaveRequest.getSendBy());

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariables(properties);

		String html = templateEngine.process("leave_request.html", context);

//		helper.setFrom(leaveRequest.getUser().getEmail());
		helper.setFrom("thong@mz.co.kr");
//		helper.setTo("thong@mz.co.kr");
		helper.setTo(leaveRequest.getSendTos().toArray(new String[leaveRequest.getSendTos().size()]));
		helper.setSubject("Leave Request");
		helper.setText(html, true);
		mailSender.send(message);
	}
}
