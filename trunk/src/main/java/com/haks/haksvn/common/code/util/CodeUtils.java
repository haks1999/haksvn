package com.haks.haksvn.common.code.util;


public class CodeUtils {

	// common
	public static boolean isTrue(String flag){
		return "common.boolean.yn.code.y".equals(flag);
	}
	
	// user
	public static boolean isSystemAdmin(String authType){
		return "user.auth.type.code.system-admin".equals(authType);
	}
	
	public static boolean isReviewer(String authType){
		return "user.auth.type.code.reviewer".equals(authType);
	}
	
	public static boolean isCommiter(String authType){
		return "user.auth.type.code.commiter".equals(authType);
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
		return stateCode ==null || stateCode.length() < 1 || "transfer.state.code.keep".equals(stateCode);
	}
	
	public static boolean isRequestableState(String stateCode){	// isDeletableState
		return "transfer.state.code.keep".equals(stateCode);
	}
	
	public static boolean isApprovableState(String stateCode){	// isRejectableState
		return "transfer.state.code.request".equals(stateCode);
	}
	
	public static boolean isRequestCancelableState(String stateCode){
		return "transfer.state.code.request".equals(stateCode);
	}
	
	public static boolean isCompleteState(String stateCode){
		return "transfer.state.code.complete".equals(stateCode) || "transfer.state.code.reject".equals(stateCode);
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
