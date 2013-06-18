package encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;

import com.haks.haksvn.common.crypto.util.CryptoUtils;

public class AES256Cipher {

	private static volatile AES256Cipher INSTANCE;

	//final static String secretKey = "12345678901234567890123456789012"; // 32bit
	final static String secretKey = "1234567890123456"; // 32bit
	static String IV = ""; // 16bit

	public static AES256Cipher getInstance() {
		if (INSTANCE == null) {
			synchronized (AES256Cipher.class) {
				if (INSTANCE == null)
					INSTANCE = new AES256Cipher();
			}
		}
		return INSTANCE;
	}

	private AES256Cipher() {
		IV = secretKey.substring(0, 16);
	}

	// 암호화
	public static String AES_Encode(String str)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] keyData = secretKey.getBytes();

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey,
				new IvParameterSpec(IV.getBytes()));

		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));

		return enStr;
	}

	// 복호화
	public static String AES_Decode(String str)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] keyData = secretKey.getBytes();
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey,
				new IvParameterSpec(IV.getBytes("UTF-8")));

		byte[] byteStr = Base64.decodeBase64(str.getBytes());

		return new String(c.doFinal(byteStr), "UTF-8");
	}
	
	public static void main(String[] args) throws Exception{
		AES256Cipher a256 = AES256Cipher.getInstance();
		System.out.println( "admin : " + a256.AES_Encode("admin") );
		System.out.println( "admin : " + CryptoUtils.encodeAES("admin") );
		System.out.println( "admin : " + a256.AES_Decode("CinA5MJWDvBTvOJSvluE4g==") );
		
		System.out.println( "haks : " + a256.AES_Encode("haks") );
		System.out.println( "ggae : " + a256.AES_Encode("ggae") );
		System.out.println( "1234 : " + a256.AES_Encode("1234") );
		System.out.println( "user01 : " + a256.AES_Encode("user01") );
		System.out.println( "user02 : " + a256.AES_Encode("user02") );
		System.out.println( "user03 : " + a256.AES_Encode("user03") );
		System.out.println( "aW9fj8bm9Rt5 : " + a256.AES_Encode("aW9fj8bm9Rt5") );
		System.out.println( "haks1999 : " + a256.AES_Encode("haks1999") );
		System.out.println( "admin : " + CryptoUtils.decodeAES("ty0VNz1v8qkwfc8Cpz1NXA==") );
		
	}
}
