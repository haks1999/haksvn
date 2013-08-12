package com.haks.haksvn.general.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.property.model.Property;
import com.haks.haksvn.common.property.service.PropertyService;
import com.haks.haksvn.common.property.util.PropertyUtils;
import com.haks.haksvn.general.model.CommitLogTemplate;
import com.haks.haksvn.general.model.MailConfiguration;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class GeneralService {

	
	@Autowired
	private PropertyService propertyService;
	
	public CommitLogTemplate retrieveDefaultCommitLogTemplate(String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		Property property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey():PropertyUtils.getCommitLogTemplateTaggingKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public CommitLogTemplate retrieveCommitLogTemplate(String repositoryKey, String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		Property property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey(repositoryKey):PropertyUtils.getCommitLogTemplateTaggingKey(repositoryKey));
		if( property == null ) property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey():PropertyUtils.getCommitLogTemplateTaggingKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).repositoryKey(repositoryKey).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public void saveCommitLogTemplate(CommitLogTemplate commitLogTemplate, String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		propertyService.saveProperty(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey(commitLogTemplate.getRepositoryKey()):PropertyUtils.getCommitLogTemplateTaggingKey(commitLogTemplate.getRepositoryKey()), commitLogTemplate.getTemplate().replaceAll("\r", "%n"));
	}
	
	public MailConfiguration retrieveMailConfiguration(){
		MailConfiguration mailConfiguration = new MailConfiguration();
		
		Property authEnabledProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpAuthEnabledKey());
		Property passwordProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpAuthPasswordKey());
		Property usernameProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpAuthUsernameKey());
		Property hostProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpHostKey());
		Property portProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpPortKey());
		Property sslEnabledProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpSslEnabledKey());
		
		if( authEnabledProp != null ) mailConfiguration.setAuthEnabled(Boolean.valueOf(authEnabledProp.getPropertyValue()));
		if( passwordProp != null ) mailConfiguration.setPassword(passwordProp.getPropertyValue());
		if( usernameProp != null ) mailConfiguration.setUsername(usernameProp.getPropertyValue());
		if( hostProp != null ) mailConfiguration.setHost(hostProp.getPropertyValue());
		if( portProp != null ) mailConfiguration.setPort(portProp.getPropertyValue());
		if( sslEnabledProp != null ) mailConfiguration.setSslEnabled(Boolean.valueOf(sslEnabledProp.getPropertyValue()));
		
		return mailConfiguration;
	}
	
	public void saveMailConfiguration(MailConfiguration mailConfiguration){
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthEnabledKey(), String.valueOf(mailConfiguration.getAuthEnabled()));
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthPasswordKey(), mailConfiguration.getPassword());
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthUsernameKey(), mailConfiguration.getUsername());
		propertyService.saveProperty(PropertyUtils.getMailSmtpHostKey(), mailConfiguration.getHost());
		propertyService.saveProperty(PropertyUtils.getMailSmtpPortKey(), mailConfiguration.getPort());
		propertyService.saveProperty(PropertyUtils.getMailSmtpSslEnabledKey(), String.valueOf(mailConfiguration.getSslEnabled()));
	}
	
	public void sendMail(MailConfiguration mailConfiguration){
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost("localhost");
		mailSenderImpl.setUsername("haks1999");
		mailSenderImpl.setPassword("haks1999");
		//Properties javaMailProperties = new Properties();
		//javaMailProperties.setProperty("mail.smtp.auth", "true");
		//javaMailProperties.setProperty("mail.smtp.starttls.enable", "false");
		//mailSenderImpl.setJavaMailProperties(javaMailProperties);
		MailSender mailSender = mailSenderImpl;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("haks1999@gmail.com");
		message.setTo("haks1999@lgcns.com");
		message.setSubject("without auth test");
		message.setText("from here dlek");
		mailSender.send(message);	
	}
}
