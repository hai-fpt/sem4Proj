package com.lms.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BaseSendMail {

	private JavaMailSender mailSender;
	private SpringTemplateEngine templateEngine;

	public BaseSendMail(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = springTemplateEngine;
	}

	protected void sendEmail(String[] tos, String[] ccTos, String subject,String htmlTemplate, Map parameters) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariables(parameters);
		String html = templateEngine.process(htmlTemplate, context);
		helper.setTo(tos);
		if (ccTos != null && ccTos.length > 0) {
			helper.setCc(ccTos);
		}
		helper.setSubject(subject);
		helper.setText(html, true);
		mailSender.send(message);
	}
}
