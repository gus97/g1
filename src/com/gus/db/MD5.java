package com.gus.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	 public static char[] encodeHex(byte[] data) {
  	   int l = data.length;
  	   char[] out = new char[l << 1];
  	   // two characters form the hex value.
  	   for (int i = 0, j = 0; i < l; i++) {
  	     out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
  	     out[j++] = DIGITS[0x0F & data[i]];
  	    }
  	   return out;
  	  }

  public static String md5(String text) {
  	MessageDigest msgDigest = null; 
  	try { 
  	msgDigest = MessageDigest.getInstance("MD5"); 
  	} catch (NoSuchAlgorithmException e) { 
  	throw new IllegalStateException("System doesn't support MD5 algorithm."); 
  	} 
  	try { 
  	msgDigest.update(text.getBytes("UTF-8")); 
  	} catch (Exception e) { 
  	throw new IllegalStateException("System doesn't support your EncodingException."); 
  	} 
  	byte[] bytes = msgDigest.digest(); 
  	String md5Str = new String(encodeHex(bytes)); 
  	return md5Str; 
  	} 


  public String killBlank(String sStr) {
      sStr = sStr.replaceAll("\r\n", "");
      sStr = sStr.replaceAll("	", "");
      return sStr.replaceAll(" ", "");
  }

//  public String getMSG() {
//  	
//  	  String FileName = "d:/11.xml";
//	    ReadXML readXML = new ReadXML();
//	    String requestXml = readXML.getXML(FileName);
//	  return requestXml;
//  }
}
