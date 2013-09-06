package com.haks.haksvn.common.code.util;


public class CodeUtils {

	// common
	public static String getCommonCodeY(){
		return "common.boolean.yn.code.y";
	}
	
	public static boolean isTrue(String flag){
		return getCommonCodeY().equals(flag);
	}
	
	// menu
	public static String getDefaultMenuViewTypeCodeId(){
		return "menu.view.type.code.default";
	}
	
	public static String getLeftMenuMenuViewTypeCodeId(){
		return "menu.view.type.code.leftmenu";
	}
	
	public static String getInvisibleMenuViewTypeCodeId(){
		return "menu.view.type.code.invisible";
	}
	
	// review
	public static String getPositiveReviewScoreCodeId(){
		return "review.score.code.positive";
	}
	
	public static String getNeutralReviewScoreCodeId(){
		return "review.score.code.neutral";
	}
	
	public static String getNegativeReviewScoreCodeId(){
		return "review.score.code.negative";
	}
	
	// user
	public static String getSystemAdminCodeId(){
		return "user.auth.type.code.system-admin";
	}
	
	public static String getReviewerCodeId(){
		return "user.auth.type.code.reviewer";
	}
	
	public static String getCommiterCodeId(){
		return "user.auth.type.code.commiter";
	}
	
	public static boolean isSystemAdmin(String authType){
		return getSystemAdminCodeId().equals(authType);
	}
	
	public static boolean isReviewer(String authType){
		return getReviewerCodeId().equals(authType);
	}
	
	public static boolean isCommiter(String authType){
		return getCommiterCodeId().equals(authType);
	}
	
	// repository
	public static boolean usingLocalConnect(String connectType){
		return "server.connect.type.code.local".equals(connectType);
	}
	
	public static boolean isMD5ApacheEncrytion(String passwdType){
		return "svn.passwd.type.code.md5-apache".equals(passwdType);
	}
	
	// transfer
	public static String getTransferEmergencyTypeCodeId(){
		return "transfer.type.code.emergency";
	}
	
	public static String getTransferKeepCodeId(){
		return "transfer.state.code.keep";
	}
	
	public static String getTransferRequestCodeId(){
		return "transfer.state.code.request";
	}
	
	public static String getTransferApprovedCodeId(){
		return "transfer.state.code.approved";
	}
	
	public static String getTransferRejectCodeId(){
		return "transfer.state.code.reject";
	}
	
	public static String getTransferStandbyCodeId(){
		return "transfer.state.code.standby";
	}
	
	public static String getTransferTransferedCodeId(){
		return "transfer.state.code.transfered";
	}
	
	public static boolean isEditableState(String stateCode){
		return stateCode ==null || stateCode.length() < 1 || getTransferKeepCodeId().equals(stateCode);
	}
	
	public static boolean isRequestableState(String stateCode){	// isDeletableState
		return getTransferKeepCodeId().equals(stateCode);
	}
	
	public static boolean isApprovableState(String stateCode){	// isRejectableState
		return getTransferRequestCodeId().equals(stateCode);
	}
	
	public static boolean isRequestCancelableState(String stateCode){
		return getTransferRequestCodeId().equals(stateCode);
	}
	
	public static boolean isApproveCancelableState(String stateCode){
		return getTransferApprovedCodeId().equals(stateCode);
	}
	
	public static boolean isApprovedState(String stateCode){
		return getTransferApprovedCodeId().equals(stateCode) || getTransferRejectCodeId().equals(stateCode);
	}
	
	public static boolean isStandbyableState(String stateCode){
		return getTransferApprovedCodeId().equals(stateCode);
	}
	
	public static boolean isTransferableState(String stateCode){
		return getTransferStandbyCodeId().equals(stateCode);
	}
	
	// transfer group
	public static String getTransferGroupNormalTypeCodeId(){
		return "transfergroup.type.code.normal";
	}
	
	public static String getTransferGroupScheduledTypeCodeId(){
		return "transfergroup.type.code.scheduled";
	}
	
	public static String getTransferGroupEmergencyTypeCodeId(){
		return "transfergroup.type.code.emergency";
	}
	
	public static String getTransferGroupStandbyCodeId(){
		return "transfergroup.state.code.standby";
	}
	
	public static String getTransferGroupTransferedCodeId(){
		return "transfergroup.state.code.transfered";
	}
	
	public static boolean isEditableTransferGroupState(String stateCode){
		return stateCode ==null || stateCode.length() < 1 || getTransferGroupStandbyCodeId().equals(stateCode);
	}
	
	// transfer source
	public static String getTransferSourceTypeDeleteCodeId(){
		return "transfer.source.type.code.delete";
	}
	
	public static String getTransferSourceTypeAddCodeId(){
		return "transfer.source.type.code.add";
	}
	
	public static String getTransferSourceTypeModifyCodeId(){
		return "transfer.source.type.code.modify";
	}
	
	public static boolean isTransferSourceTypeAdd( String code ){
		return getTransferSourceTypeAddCodeId().equals(code);
	}
	
	public static boolean isTransferSourceTypeDelete( String code ){
		return getTransferSourceTypeDeleteCodeId().equals(code);
	}
	
	public static boolean isTransferSourceTypeModify( String code ){
		return getTransferSourceTypeModifyCodeId().equals(code);
	}
	
	
	// tagging
	public static String getTaggingCreateCodeId(){
		return "tagging.type.code.create";
	}
		
	public static String getTaggingRestoreCodeId(){
		return "tagging.type.code.restore";
	}
	
	public static boolean isTaggingCreateType(String code ){
		return getTaggingCreateCodeId().equals(code);
	}
	
	public static boolean isTaggingRestoreType(String code){
		return getTaggingRestoreCodeId().equals(code);
	}
	
	// log template
	public static String getLogTemplateRequestCodeId(){
		return "log.template.type.code.request";
	}
	
	public static String getLogTemplateTaggingCodeId(){
		return "log.template.type.code.tagging";
	}
	
	public static boolean isLogTemplateRequest(String code){
		return getLogTemplateRequestCodeId().equals(code);
	}
	
	public static boolean isLogTemplateTagging(String code){
		return getLogTemplateTaggingCodeId().equals(code);
	}
	
	// mail template
	public static String getMailTemplateReviewRequestCodeId(){
		return "mail.template.type.code.review.request";
	}
	
	public static boolean isMailTemplateReviewRequest(String code){
		return getMailTemplateReviewRequestCodeId().equals(code);
	}
	
	public static String getMailTemplateTransferRequestCodeId(){
		return "mail.template.type.code.transfer.request";
	}
	
	public static boolean isMailTemplateTransferRequest(String code){
		return getMailTemplateTransferRequestCodeId().equals(code);
	}
	
	public static String getMailTemplateTransferRejectCodeId(){
		return "mail.template.type.code.transfer.reject";
	}
	
	public static boolean isMailTemplateTransferReject(String code){
		return getMailTemplateTransferRejectCodeId().equals(code);
	}
	
	public static String getMailTemplateTransferApproveCodeId(){
		return "mail.template.type.code.transfer.approve";
	}
	
	public static boolean isMailTemplateTransferApprove(String code){
		return getMailTemplateTransferApproveCodeId().equals(code);
	}
	
	public static String getMailTemplateTransferCompleteCodeId(){
		return "mail.template.type.code.transfer.complete";
	}
	
	public static boolean isMailTemplateTransferComplete(String code){
		return getMailTemplateTransferCompleteCodeId().equals(code);
	}
	
	// mail notice
	public static String getMailNoticeReviewRequestCodeId(){
		return "mail.notice.type.code.review.request";
	}
	
	public static String getMailNoticeTransferRequestCodeId(){
		return "mail.notice.type.code.transfer.request";
	}
	
	public static String getMailNoticeTransferRejectCodeId(){
		return "mail.notice.type.code.transfer.reject";
	}
	
	public static String getMailNoticeTransferApproveCodeId(){
		return "mail.notice.type.code.transfer.approve";
	}
	
	public static String getMailNoticeTransferCompleteCodeId(){
		return "mail.notice.type.code.transfer.complete";
	}
}