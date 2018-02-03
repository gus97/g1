package com.gopay.utils.hnbx;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 使用BASE64对字符串的加解密
 * 
 * @author LuoGang
 * 
 */
public class BASE64Helper {

	/**
	 * 将byte数组编码为字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * 将字符串还原为byte数组
	 * 
	 * @param text
	 * @return
	 */
	public static byte[] decode(String text) {
		try {
			return new BASE64Decoder().decodeBuffer(text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
