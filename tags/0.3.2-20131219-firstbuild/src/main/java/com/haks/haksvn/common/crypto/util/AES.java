package com.haks.haksvn.common.crypto.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

class AES {

	// 256 으로 할려믄 jre security 바까주야함
	//private final static String secretKey = "12345678901234567890123456789012"; // 32bit
	private final static String secretKey = "1234567890123456"; // 32bit
	private final static String IV = secretKey;

	private AES() {
	}

	public static String encode(String str) {
		String enStr = "";
		byte[] keyData = secretKey.getBytes();
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		try{
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			enStr = new String(Base64.encodeBase64(encrypted));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return enStr;
	}

	public static String decode(String str){
		String deStr = "";
		byte[] keyData = secretKey.getBytes();
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		try{
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey,	new IvParameterSpec(IV.getBytes("UTF-8")));
			byte[] byteStr = Base64.decodeBase64(str.getBytes());
			deStr = new String(c.doFinal(byteStr), "UTF-8");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return deStr;
	}
}
