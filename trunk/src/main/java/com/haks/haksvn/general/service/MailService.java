package com.haks.haksvn.general.service;

import java.util.Properties;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class MailService {

	private MailSender mailSender;

	public void sendMail() {
		
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost("localhost");
		mailSenderImpl.setUsername("haks1999");
		mailSenderImpl.setPassword("haks1999");
		//Properties javaMailProperties = new Properties();
		//javaMailProperties.setProperty("mail.smtp.auth", "true");
		//javaMailProperties.setProperty("mail.smtp.starttls.enable", "false");
		//mailSenderImpl.setJavaMailProperties(javaMailProperties);
		mailSender = mailSenderImpl;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("haks1999@gmail.com");
		message.setTo("haks1999@lgcns.com");
		message.setSubject("without auth test");
		message.setText("from here dlek");
		mailSender.send(message);	
	}
	
	public static void main(String[] args){
		new MailService().sendMail();
	}
}
