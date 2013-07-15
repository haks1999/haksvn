package com.haks.haksvn.common.crypto.util;


public class CryptoUtils {

	public static String encodeMD5Apache(String text){
		return MD5.apacheCrypt(text);
	}
	
	public static String encodeAES(String text){
		return AES.encode(text);
	}
	
	public static String decodeAES(String text){
		return AES.decode(text);
	}
}
