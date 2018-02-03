package com.gus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClientTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		EServerManageService ems = new EServerManageService();
		EServerManage em = ems.getPort(EServerManage.class);
		
		@SuppressWarnings("resource")
		BufferedReader br=new BufferedReader(new FileReader("d:/data/tb.xml"));
		String line="";
		StringBuffer  buffer = new StringBuffer();
		while((line=br.readLine())!=null){
		buffer.append(line);
		}
		String fileContent = buffer.toString();
		
		//String s = em.getServerXML(fileContent);
		
		String s = em.cancelPolicy(fileContent, "46");
		
		System.out.println(s);
	}

}
