package com.gopay.utils.hnbx;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA算法，本系统主要用于登录的用户名和密码加密.
 * 
 * RSA 很少用来做加密算法使用，一般多数用于数据签名算法中。
 * 这是由于 RSA 作为加密算法进行加密处理时，其所能处理的原文数据量不能超过 （RSA 长度 / 8 - 11），
 * 比如：RSA 1024 进行加密运算时，原文的长度不能超过 117 个字节。
 * 
 * @author LuoGang
 *
 */
public class RSAHelper {
	/** 默认的密钥长度 */
	private static final int DEFAULT_KEY_SIZE = NumberHelper.intValue(SystemHelper.getProperty("rsa.key.size"), 1024);
	/** RSA加密算法 */
	private static final String ALGORITHOM = "RSA";
	/** 字符串默认使用的编码 */
	public static final String DEFAULT_CHARSET_NAME = SystemHelper.ENCODING;


	public static KeyPair generateKeyPair() {
		return generateKeyPair(DEFAULT_KEY_SIZE);
	}

	/**
	 * 生成一个密钥对
	 * 
	 * @param keySize 密钥长度
	 * @return
	 */
	public static KeyPair generateKeyPair(int keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM);
			keyPairGen.initialize(keySize);
			return keyPairGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据字符串生成公钥
	 * 
	 * @param publicKeyString 经过base64编码的公钥字符串
	 */
	public static PublicKey generatePublic(String publicKeyString) {
		try {
			KeyFactory kf = KeyFactory.getInstance(ALGORITHOM);
			return kf.generatePublic(new X509EncodedKeySpec(BASE64Helper.decode(publicKeyString)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据字符串生成私钥
	 * 
	 * @param privateKeyString 经过base64编码的私钥字符串
	 */
	public static PrivateKey generatePrivate(String privateKeyString) {
		try {
			KeyFactory kf = KeyFactory.getInstance(ALGORITHOM);
			return kf.generatePrivate(new PKCS8EncodedKeySpec(BASE64Helper.decode(privateKeyString)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用key对数据input加密
	 * 
	 * @param input 要加密的数据
	 * @param key 密钥
	 * @return
	 */
	public static byte[] encode(byte[] input, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHOM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] bytes = cipher.doFinal(input);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用key对字符串inputString加密
	 * 
	 * @param input 明文字符串
	 * @param key 密钥
	 * @param charsetName 字符串的编码方式
	 * @return 使用BASE64加密后的密文字符串
	 * @see BASE64Helper#encode(byte[])
	 */
	public static String encode(String input, Key key, String charsetName) {
		// byte[] input;
		// try {
		// input = StringHelper.isEmpty(charsetName) ? inputString.getBytes() : inputString.getBytes(charsetName);
		// } catch (Exception e) {
		// input = inputString.getBytes();
		// }
		byte[] bytes = encode(StringHelper.toBytes(input, charsetName), key);
		return BASE64Helper.encode(bytes);
	}

	/**
	 * 
	 * 使用key对字符串inputString加密
	 * 
	 * @param input 明文字符串
	 * @param key 密钥
	 * @return 使用BASE64加密后的密文字符串
	 * @see #encode(String, Key, String)
	 * @see BASE64Helper#encode(byte[])
	 */
	public static String encode(String input, Key key) {
		byte[] bytes = encode(StringHelper.toBytes(input, DEFAULT_CHARSET_NAME), key);
		return BASE64Helper.encode(bytes);
	}

	/**
	 * 使用key对数据input解密
	 * 
	 * @param input 要解密的数据
	 * @param key 密钥
	 * @return
	 */
	public static byte[] decode(byte[] input, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHOM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] bytes = cipher.doFinal(input);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用key对密文字符串inputString解密
	 * 
	 * @param inputString 使用BASE64加密的密文字符串
	 * @param key 密钥
	 * @param charsetName 字符串的编码方式
	 * @return 使用charsetName编码的密文字符串
	 * @see BASE64Helper#decode(byte[])
	 */
	public static String decode(String inputString, Key key, String charsetName) {
		byte[] input = BASE64Helper.decode(inputString);
		byte[] bytes = decode(input, key);
		// String output;
		// try {
		// output = StringHelper.isEmpty(charsetName) ? new String(bytes) : new String(bytes, charsetName);
		// } catch (Exception e) {
		// output = new String(bytes);
		// }
		// return output;

		return StringHelper.valueOf(bytes, charsetName);
	}

	
	public static String decode(String inputString, Key key) {
		byte[] input = BASE64Helper.decode(inputString);
		byte[] bytes = decode(input, key);
		// try {
		// return new String(bytes, DEFAULT_CHARSET_NAME);
		// } catch (UnsupportedEncodingException e) {
		// return new String(bytes);
		// }
		return StringHelper.valueOf(bytes, DEFAULT_CHARSET_NAME);
	}

	public static void main(String[] args) {
		String msg = "Json报文";
		String vmd5 = MD5Helper.encode(msg, "UTF-8");
		vmd5 = vmd5.toUpperCase();
		System.out.println("摘要" + vmd5);
//		String priKey = SystemHelper.getProperty("rsa.key.private");
		String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSIwNTKOzclWzW0H/oGpLG88dAGkpHbSoqFuR6+uvF5x5THsYBpTwik/viJPahiAWiOYFS0iVQEJamr7+wf3WhG1QIB+lCRoHdWUzly0icEUpRLVw5QH36IpdqH9WQ+GyuTJbt1ml4BeAKyB8A1iUnO1YHJtpAfU7wOnqiaHVs7AgMBAAECgYAL3Wi6b5LxmRoIt2KO7ye1QYDWlYLATeYEMzx0QSDOBRAlZHkiX3W5k9xnI850dAzR82Jy+Unl6x6kK6fziSC4JLjS4jSXwwbO0zfJFDViRBwijowN46LPw6f8MOk/Ao+F+gvxXaXfIz9rl8NibfSwjj/kg60G9NQRTa6SliWckQJBANCwiWemEmd00leQ257A+1J+xCO/T3ubjypcre0m2RE1ph7Ht5M30VKIiMcOymoBCeu9REErWEsM5JvSET9lLS8CQQDJ1Z+MYcll8QrUlUhFCY+3jCqUOLXa3omBngKhOqsJcQey4cjc5q2Onu+5aNyWmmwlX2BVN4w7P+ahENQzYee1AkBXJREL1j6jMxZO+K+tOHr6dPMk1lOzkz8Seocqm+lTFWscOVwAPvPLGja7oQKHh42EApIJALerB2RDaVJIfdCDAkEAqZcI77D+w9xsqEEjL3cgPi7VXCPmkhho2/OD7CtaqZ1Cxci3uP1PGcZsUZNNvnHUqvZgAFqjRumjhPSaCO6b/QJBAIMB7Q3CfoJUpApa+8l9+utdTM3+RxLrjo14c1PBjwV+zG1nuLKyJLOo+b+xSECgWCqYTWQQTamZ72xE6YHw8vw=";
		String sign = RSAHelper.encode(vmd5,
				RSAHelper.generatePrivate(priKey));
		System.out.println("sign:" + sign);
	}
}
