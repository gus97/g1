package com.gopay.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Des3 {

	// 向量
	   //private final static String iv = "01234567";
	   
	   //private static byte[] iv1 = {1,2,3,4,5,6,7,8};
	   // 加解密统一使用的编码方式
	   private final static String encoding = "utf-8";
	   
	   public static String fillstr(String str)
	   {  
		   int strlen = str.length();  
		   for(int i=1;i<=24-strlen;i++)
		   {  
			   str += "1";  
		   }  
		   return str;
	   }
	   
	   public static String fillstr2(String str)
	   {  
		   return str.substring(str.length()-8,str.length());
	   }
	   /**
	  * 3DES加密
	  * 
	  * @param plainText 普通文本
	  * @return
	  * @throws Exception 
	  */
	   public static String encode(String secretKey,String plainText){
		   try
		   {		   
			   secretKey=fillstr2(secretKey);
		       SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");  
		       
		       Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		       cipher.init(Cipher.ENCRYPT_MODE, key);
		       byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		       return returnWebStr(Base64.encode(encryptData));
		   }catch(Exception e)
		   {
			   System.out.println(e.getMessage());
			   return null;
		   }

	   }

	   /**
	  * 3DES解密
	  * 
	  * @param encryptText 加密文本
	  * @return
	  * @throws Exception
	  */
	   public static String decode(String secretKey,String encryptText){
		   try
		   {
		   encryptText = returnRealStr(encryptText);	    
		   secretKey=fillstr2(secretKey);
	       SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");  
	       Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	       cipher.init(Cipher.DECRYPT_MODE, key);  
	       
	       byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

	       return new String(decryptData, encoding);
		   }catch(Exception e)
		   {
			   System.out.println(e.getMessage());
			   return null;
		   }
	   }
	   
	   private static String returnWebStr(String str)
		{
			str=str.replace("%","%25");
			str=str.replace("&","%26");
			str=str.replace("+","%2B");
			return str;
		}
		
	   private static String returnRealStr(String str)
		{
			str=str.replace("%25","%");
			str=str.replace("%26","&");
			str=str.replace("%2B","+");
			return str;
		}
		
		public static void main(String[] args) {
			
			String mis="piccandthirdpart";//密匙
			String word="00111699000001";//明文picchtestname
			System.out.println("加密内容："+encode(mis,word));
			//124.127.37.57:8088/ejb/thirdCheckPDF.jsp?contno=nJdxIlPhPMrE2zmYQ56gCw==
			
			System.out.println("解密内容："+decode(mis,"iia5ZNfe2YEzBUvMcBXn0LKlmmKfpFEY"));
			//System.out.println(decode(mis,"XwxnynGMehy6HGp5a44k0Q=="));
		}
}
