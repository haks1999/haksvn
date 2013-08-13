package com.haks.haksvn.common.property.util;

public class PropertyUtils {

	public static String getCommitLogTemplateRequestKey(){
		return "commit.log.template.request.default";
	}
	
	public static String getCommitLogTemplateRequestKey(String repositoryKey){
		return "commit.log.template.request." + repositoryKey;
	}
	
	public static String getCommitLogTemplateTaggingKey(){
		return "commit.log.template.tagging.default";
	}
	
	public static String getCommitLogTemplateTaggingKey(String repositoryKey){
		return "commit.log.template.tagging." + repositoryKey;
	}
	
	public static String getApplicationVersionKey(){
		return "application.version";
	}
	
	public static String getMailSmtpHostKey(){
		return "mail.smtp.host";
	}
	
	public static String getMailSmtpPortKey(){
		return "mail.smtp.port";
	}
	
	public static String getMailSmtpReplytoKey(){
		return "mail.smtp.replyto";
	}
	
	public static String getMailSmtpSslEnabledKey(){
		return "mail.smtp.ssl.enabled";
	}
	
	public static String getMailSmtpAuthEnabledKey(){
		return "mail.smtp.auth.enabled";
	}
	
	public static String getMailSmtpAuthUsernameKey(){
		return "mail.smtp.auth.username";
	}
	
	public static String getMailSmtpAuthPasswordKey(){
		return "mail.smtp.auth.password";
	}
	
	public static String getMailTemplateReviewRequestSubjectKey(){
		return "mail.template.review.request.subject.default";
	}
	
	public static String getMailTemplateReviewRequestSubjectKey(String repositoryKey){
		return "mail.template.review.request.subject." + repositoryKey;
	}
	
	public static String getMailTemplateReviewRequestTextKey(){
		return "mail.template.review.request.text.default";
	}
	
	public static String getMailTemplateReviewRequestTextKey(String repositoryKey){
		return "mail.template.review.request.text." + repositoryKey;
	}
}
