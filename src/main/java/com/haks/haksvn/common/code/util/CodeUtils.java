package com.haks.haksvn.common.code.util;

import com.haks.haksvn.common.crypto.MD5Crypt;

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
}
