package com.teleios.pos.service;

import java.io.IOException;
import java.io.Serializable;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;

@Service
public class EmailService implements Serializable {
	private static final long serialVersionUID = -7151148605822438205L;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	private JavaMailSender mailSender;
	private ReportService reportService;

	@Autowired
	public EmailService(JavaMailSender mailSender, ReportService reportService) {
		super();
		this.mailSender = mailSender;
		this.reportService = reportService;
	}

	public void sendMail(Integer grnNumber) throws JRException, IOException, MessagingException, Exception {
		LOGGER.info("<-------- Execute Send Mail In Email Serice --------->");
		String from = "ahangamaharith1@gmail.com";
		// String to = "teleioslk@gmail.com";
		String[] recepiants = new String[] { "ahangamaharith1@gmail.com", "teleioslk@gmail.com" };

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(recepiants);
		helper.setSubject("This is a Grn For Below Atached GRN No:" + grnNumber);
		helper.setText("<b>Dear Sir</b>,<br><i>Please Refer This attached GRN And Confirm It .</i>", true);
		helper.addAttachment("GRN Report :" + grnNumber + ".pdf", this.reportService.getGrnAtachment(grnNumber));

		this.mailSender.send(message);

		LOGGER.info("<-------- Send Grn Report Email Success  --------->");
	}
}
