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
	
	
	// mail template
	public static String getMailTemplateReviewRequestSubjectKey(){
		return "mail.template.review.request.subject.default";
	}
	
	public static String getMailTemplateReviewRequestSubjectKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateReviewRequestSubjectKey();
		return "mail.template.review.request.subject." + repositoryKey;
	}
	
	public static String getMailTemplateReviewRequestTextKey(){
		return "mail.template.review.request.text.default";
	}
	
	public static String getMailTemplateReviewRequestTextKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateReviewRequestTextKey();
		return "mail.template.review.request.text." + repositoryKey;
	}
	
	public static String getMailTemplateTransferRequestSubjectKey(){
		return "mail.template.transfer.request.subject.default";
	}
	
	public static String getMailTemplateTransferRequestSubjectKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferRequestSubjectKey();
		return "mail.template.transfer.request.subject." + repositoryKey;
	}
	
	public static String getMailTemplateTransferRequestTextKey(){
		return "mail.template.transfer.request.text.default";
	}
	
	public static String getMailTemplateTransferRequestTextKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferRequestTextKey();
		return "mail.template.transfer.request.text." + repositoryKey;
	}
	
	public static String getMailTemplateTransferRejectSubjectKey(){
		return "mail.template.transfer.reject.subject.default";
	}
	
	public static String getMailTemplateTransferRejectSubjectKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferRejectSubjectKey();
		return "mail.template.transfer.reject.subject." + repositoryKey;
	}
	
	public static String getMailTemplateTransferRejectTextKey(){
		return "mail.template.transfer.reject.text.default";
	}
	
	public static String getMailTemplateTransferRejectTextKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferRejectTextKey();
		return "mail.template.transfer.reject.text." + repositoryKey;
	}
	
	public static String getMailTemplateTransferApproveSubjectKey(){
		return "mail.template.transfer.approve.subject.default";
	}
	
	public static String getMailTemplateTransferApproveSubjectKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferApproveSubjectKey();
		return "mail.template.transfer.approve.subject." + repositoryKey;
	}
	
	public static String getMailTemplateTransferApproveTextKey(){
		return "mail.template.transfer.approve.text.default";
	}
	
	public static String getMailTemplateTransferApproveTextKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferApproveTextKey();
		return "mail.template.transfer.approve.text." + repositoryKey;
	}
	
	public static String getMailTemplateTransferCompleteSubjectKey(){
		return "mail.template.transfer.complete.subject.default";
	}
	
	public static String getMailTemplateTransferCompleteSubjectKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferCompleteSubjectKey();
		return "mail.template.transfer.complete.subject." + repositoryKey;
	}
	
	public static String getMailTemplateTransferCompleteTextKey(){
		return "mail.template.transfer.complete.text.default";
	}
	
	public static String getMailTemplateTransferCompleteTextKey(String repositoryKey){
		if( repositoryKey == null || repositoryKey.length() < 1 ) return getMailTemplateTransferCompleteTextKey();
		return "mail.template.transfer.complete.text." + repositoryKey;
	}
}
