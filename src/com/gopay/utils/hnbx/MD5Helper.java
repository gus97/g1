package com.gopay.utils.hnbx;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用MD5加密字符串
 * 
 * @author LuoGang
 * 
 */
public class MD5Helper {
	/** MD5加密算法 */
	private static final String ALGORITHOM = "MD5";

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text 明文
	 * @param encoding 明文使用的编码
	 * 
	 * @return 密文
	 */
	public static String encode(String text, String encoding) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance(ALGORITHOM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			// throw new MortalException(e);
		}
		text = text == null ? "" : text;
		try {
			md5.update(StringHelper.isEmpty(encoding) ? text.getBytes() : text.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			md5.update(text.getBytes());
		}

		byte[] bytes = md5.digest();
		return NumberHelper.toHexString(bytes);
	}

	/**
	 * 用MD5加密字符串,使用系统默认的编码
	 * 
	 * @param text 要加密的字符串
	 * @return 密文
	 * 
	 * @see SystemHelper#ENCODING
	 */
	public static String encode(String text) {
		return encode(text, SystemHelper.ENCODING);
	}

	/**
	 * 用MD5加密字符串
	 * 
	 * @param text 要加密的字符串
	 * @return 密文
	 */
	public static String md5(String text) {
		return encode(text);
	}

	public static void main(String[] args) {
		System.out.println(encode("1"));
	}
}
