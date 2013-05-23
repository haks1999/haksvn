package com.haks.haksvn.common.code.util;


public class CodeUtils {

	// common
	public static boolean isTrue(String flag){
		return "common.boolean.yn.code.y".equals(flag);
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
	public static String getTransferKeepCodeId(){
		return "transfer.state.code.keep";
	}
	
	public static String getTransferRequestCodeId(){
		return "transfer.state.code.request";
	}
	
	public static String getTransferCompleteCodeId(){
		return "transfer.state.code.complete";
	}
	
	public static String getTransferRejectCodeId(){
		return "transfer.state.code.reject";
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
	
	public static boolean isCompleteState(String stateCode){
		return getTransferCompleteCodeId().equals(stateCode) || getTransferRejectCodeId().equals(stateCode);
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
	
	public static boolean isTransferSourceTypeDelete( String code ){
		return getTransferSourceTypeDeleteCodeId().equals(code);
	}
}
