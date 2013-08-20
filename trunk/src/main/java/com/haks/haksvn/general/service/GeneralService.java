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
import com.haks.haksvn.general.model.MailMessage;
import com.haks.haksvn.general.model.MailTemplate;

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
		Property replyto = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailSmtpReplytoKey());
		
		if( authEnabledProp != null ) mailConfiguration.setAuthEnabled(Boolean.valueOf(authEnabledProp.getPropertyValue()));
		if( passwordProp != null ) mailConfiguration.setPassword(passwordProp.getPropertyValue());
		if( usernameProp != null ) mailConfiguration.setUsername(usernameProp.getPropertyValue());
		if( hostProp != null ) mailConfiguration.setHost(hostProp.getPropertyValue());
		if( portProp != null ) mailConfiguration.setPort(portProp.getPropertyValue());
		if( sslEnabledProp != null ) mailConfiguration.setSslEnabled(Boolean.valueOf(sslEnabledProp.getPropertyValue()));
		if( replyto != null ) mailConfiguration.setReplyto(replyto.getPropertyValue());

		return mailConfiguration;
	}
	
	public void saveMailConfiguration(MailConfiguration mailConfiguration){
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthEnabledKey(), String.valueOf(mailConfiguration.getAuthEnabled()));
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthPasswordKey(), mailConfiguration.getPassword());
		propertyService.saveProperty(PropertyUtils.getMailSmtpAuthUsernameKey(), mailConfiguration.getUsername());
		propertyService.saveProperty(PropertyUtils.getMailSmtpHostKey(), mailConfiguration.getHost());
		propertyService.saveProperty(PropertyUtils.getMailSmtpPortKey(), mailConfiguration.getPort());
		propertyService.saveProperty(PropertyUtils.getMailSmtpSslEnabledKey(), String.valueOf(mailConfiguration.getSslEnabled()));
		propertyService.saveProperty(PropertyUtils.getMailSmtpReplytoKey(), mailConfiguration.getReplyto());
	}
	
	public void sendMail(MailConfiguration mailConfiguration, MailMessage mailMessage){
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost( mailConfiguration.getHost());
		mailSenderImpl.setPort(Integer.valueOf(mailConfiguration.getPort()));
		java.util.Properties javaMailProperties = new java.util.Properties();
		javaMailProperties.setProperty("mail.smtp.auth", String.valueOf(mailConfiguration.getAuthEnabled()));
		javaMailProperties.setProperty("mail.smtp.ssl.enable", String.valueOf(mailConfiguration.getSslEnabled()));
		if( mailConfiguration.getSslEnabled() ){
			mailSenderImpl.setProtocol("smtps");
			javaMailProperties.setProperty("mail.smtps.auth", "true");
			javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
			javaMailProperties.setProperty("mail.transport.protocol", "smtps");
			//javaMailProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			//javaMailProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
			
		}
		mailSenderImpl.setJavaMailProperties(javaMailProperties);
		if( mailConfiguration.getAuthEnabled() ){
			mailSenderImpl.setUsername( mailConfiguration.getUsername());
			mailSenderImpl.setPassword( mailConfiguration.getPassword());
		}
		
		MailSender mailSender = mailSenderImpl;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom( mailMessage.getFrom());
		message.setTo(mailMessage.getTo());
		message.setSubject(mailMessage.getSubject());
		message.setText(mailMessage.getText());
		mailSender.send(message);	
	}
	
	public MailTemplate retrieveDefaultMailTemplate(String mailTemplateCodeId){
		//boolean isReviewRequestType = CodeUtils.isMailTemplateReviewRequest(mailTemplateCodeId);
		Property subjectProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestSubjectKey());
		Property textProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestTextKey());
		return MailTemplate.Builder.getBuilder().subject(subjectProp.getPropertyValue().replaceAll("%n", "\r")).text(textProp.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public MailTemplate retrieveMailTemplate(String repositoryKey, String mailTemplateCodeId){
		Property subjectProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestSubjectKey(repositoryKey));
		Property textProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestTextKey(repositoryKey));
		if( subjectProp == null ) subjectProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestSubjectKey());
		if( textProp == null ) textProp = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getMailTemplateReviewRequestTextKey());
		return MailTemplate.Builder.getBuilder().repositoryKey(repositoryKey).subject(subjectProp.getPropertyValue().replaceAll("%n", "\r")).text(textProp.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public void saveMailTemplate(MailTemplate mailTemplate, String templateCodeId){
		propertyService.saveProperty(PropertyUtils.getMailTemplateReviewRequestSubjectKey(mailTemplate.getRepositoryKey()), mailTemplate.getSubject().replaceAll("\r", "%n"));
		propertyService.saveProperty(PropertyUtils.getMailTemplateReviewRequestTextKey(mailTemplate.getRepositoryKey()), mailTemplate.getText().replaceAll("\r", "%n"));
	}
	
}
