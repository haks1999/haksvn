package com.haks.haksvn.repository.util;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.crypto.util.CryptoUtils;
import com.haks.haksvn.repository.model.Repository;

public class RepositoryUtils {
	
	public static String encryptPasswd(String passwd, String passwdType){
		if( CodeUtils.isMD5ApacheEncrytion(passwdType) ) return CryptoUtils.encodeMD5Apache(passwd);
		return passwd;
	}

	public static String getPasswdFileDelimeter(String passwdType ) {
		if( CodeUtils.isMD5ApacheEncrytion(passwdType) ) return ":";
		return "=";
	}
	
	public static String getFormattedAuthzTemplate(String template){
		return template.replaceAll("\r\n", "%n").replaceAll("\n","%n");
	}
	
	public static String getRelativeRepositoryPath(Repository repository, String path){
		String relativeRoot = repository.getRepositoryLocation().substring(repository.getSvnRoot().length());
		if( relativeRoot.startsWith("/")) relativeRoot.replaceFirst("/", "");
		if( path.length() > 0 && !relativeRoot.endsWith("/")) relativeRoot = relativeRoot + "/";
		return relativeRoot + path;
	}
}
