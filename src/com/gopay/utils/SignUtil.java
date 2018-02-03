package com.gopay.utils;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;


public class SignUtil {

	 //供外部调用的方法。
	 public static void run(String data,String privateKey,String publicKey,String algorithm) throws GeneralSecurityException, Exception{
	     
	Base64 b64 = new Base64();
		 String dataEncode = b64.encodeToString(data.getBytes("UTF-8"));
	        byte[] signBytes = sign(dataEncode.getBytes(), b64.decode(privateKey), algorithm);
	        String sign = b64.encodeToString(signBytes);
	        dataEncode = b64.encodeToString(data.getBytes("UTF-8"));
	        System.err.println(sign);
	        boolean verify = verify(dataEncode.getBytes(), b64.decode(sign),
	            b64.decode(publicKey), algorithm);

	        System.out.println(verify);
			
	}
	 //RSA验签名检查 
	 //text:待签名数据，signedText:签名值，publicKeyData：开发商公钥
	public static boolean verify(final byte[] text, final byte[] signedText,
			final byte[] publicKeyData, final String algorithm)
			throws GeneralSecurityException {
		//构造PKCS8EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyData);
		//使用RSA指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		//取公钥的对象
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		Signature signatureChecker = Signature.getInstance(algorithm);
		signatureChecker.initVerify(publicKey);
		signatureChecker.update(text);
		//验证签名是否正常
		return signatureChecker.verify(signedText);
	}
	//RSA签名 
	//text:待签名的数据，privateKeyData：第三方的私钥
    public static byte[] sign(final byte[] text, final byte[] privateKeyData,
			final String algorithm) throws GeneralSecurityException {
    	//构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
		//使用RSA指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		//取私钥对象
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		//用私钥对信息生成数字签名
		Signature signatureChecker = Signature.getInstance(algorithm);
		signatureChecker.initSign(privateKey);
		signatureChecker.update(text);
		return signatureChecker.sign();
	}
}
