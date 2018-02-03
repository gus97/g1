package com.gopay.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.jdbc.core.JdbcTemplate;

public class YTBX {

	public static String ytbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) {

		System.out.println(companyid + "---" + projectid + "---" + medid);

		final StringBuffer buff_xml = new StringBuffer();
		// ===========================head===============================
		buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		buff_xml.append("<persons>");

		buff_xml.append("<plyAppNo>" + BatchNo + "</plyAppNo>");

		List<Map<String, Object>> teamorderList = jdbcTemplate
				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });

		buff_xml.append("<insuredCname>" + teamorderList.get(0).get("grpName") + "</insuredCname>");

		String organcomCode = (String) teamorderList.get(0).get("organcomCode");
		String taxNo = (String) teamorderList.get(0).get("taxNo");
		String creditCode = (String) teamorderList.get(0).get("creditCode");
		String businessCode = (String) teamorderList.get(0).get("businessCode");
		String insuredCertNo = "";
		if (organcomCode != null && !"--".equals(organcomCode) && !"".equals(organcomCode)) {
			insuredCertNo = organcomCode;
		} else if (taxNo != null && !"--".equals(taxNo) && !"".equals(taxNo)) {
			insuredCertNo = taxNo;
		} else if (creditCode != null && !"--".equals(creditCode) && !"".equals(creditCode)) {
			insuredCertNo = creditCode;
		} else if (businessCode != null && !"--".equals(businessCode) && !"".equals(businessCode)) {
			insuredCertNo = businessCode;
		} else {
			return "insuredCertNo is null";
		}
		buff_xml.append("<insuredCertNo>" + insuredCertNo + "</insuredCertNo>");

		buff_xml.append(
				"<beginTime>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10) + "</beginTime>");
		buff_xml.append(
				"<endTime>" + teamorderList.get(0).get("CInValiDate").toString().substring(0, 10) + "</endTime>");

		List<Map<String, Object>> teampersonBBList = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });

		for (Map<String, Object> map : teampersonBBList) {

			buff_xml.append("<person>");

			buff_xml.append("<insrntCname>" + map.get("InsuredName") + "</insrntCname>");

			int idType = (int) map.get("IDType");

			String st = "";

			if (idType == 0) {
				st = "01";
			} else if (idType == 5) {
				st = "02";
			} else if (idType == 1) {
				st = "03";
			} else if (idType == 2) {
				st = "04";
			} else if (idType == 3) {
				st = "05";
			} else {
				st = "99";
			}

			buff_xml.append("<insrntCertType>" + st + "</insrntCertType>");

			buff_xml.append("<insrntCertNo>" + map.get("IDNo") + "</insrntCertNo>");

//			buff_xml.append("<insrntEmail>13951774238@139.com</insrntEmail>");

//			buff_xml.append("<insrntPhone>13333333333</insrntPhone>");

			int pid = (int) map.get("personid");

			int pid2 = 1;

			if (pid == 1 || pid == 2) {
				pid2 = 1;
			} else if (pid == 3 || pid == 4) {
				pid2 = 2;
			} else if (pid == 5 || pid == 6) {
				pid2 = 3;
			} else {
				return "pid Error";
			}

			buff_xml.append("<vfactor1>" + pid2 + "</vfactor1>");
			buff_xml.append("</person>");
		}

		buff_xml.append("</persons>");

		System.out.println(buff_xml.toString());

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost("http://e-policy.apiins.com/InsertTeamServlet.do");

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();


		List<Map<String, Object>> riskPlanCode = jdbcTemplate.queryForList(
				"SELECT CODE FROM Insproject WHERE Insproject.id = ?",
				new Object[] { projectid });
		
		System.out.println(riskPlanCode.get(0).get("CODE"));
		
		formparams.add(new BasicNameValuePair("riskPlanCode", (String)riskPlanCode.get(0).get("CODE")));
		formparams.add(new BasicNameValuePair("userCode", "MA001067"));
		formparams.add(new BasicNameValuePair("password", "7C3B871A578D6EB4C1F591B0139B63CD"));
		formparams.add(new BasicNameValuePair("xmlData", buff_xml.toString()));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			InputStreamReader reader2 = new InputStreamReader(resEntity.getContent(), "UTF-8");
			char[] buff = new char[1024];
			int length = 0;
			String t = "";
			while ((length = reader2.read(buff)) != -1) {
				t += new String(buff, 0, length);
			}
			System.out.println(t + "=========");

			Pattern p = Pattern.compile("<code>(.*)</code>");
			Matcher m = p.matcher(t);

			while (m.find()) {
				if (!"0000".equals(m.group(1))) {
					Pattern p1 = Pattern.compile("<msg>(.*)</msg> ");
					Matcher m1 = p1.matcher(t);
					while (m1.find()) {
						return m1.group(1);
					}
				} else {
					Pattern p1 = Pattern.compile("<policyNo>(.*)</policyNo>");
					Matcher m1 = p1.matcher(t);
					while (m1.find()) {
						jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,GrpContNo = ? WHERE batchno = ?",
								new Object[] { 7, m1.group(1), BatchNo });
						jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? WHERE batchno = ?",
								new Object[] {
										"http://e-policy.apiins.com/TeamPdfServlet.do?policyNo=" + m1.group(1),
										BatchNo });
						System.out.println("http://e-policy.apiins.com/TeamPdfServlet.do?policyNo=" + m1.group(1));
					}

					return "0000";
				}
			}
		} catch (ClientProtocolException e) {
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[] { 6, BatchNo });
			jdbcTemplate.update(
					"INSERT INTO orderlog  (batchno,front,behind,note,dealtime) VALUES (?,5,6,'发送保险网关平台失败1001',now())",
					new Object[] { BatchNo });
			return "err:发送保险网关平台失败1001";
		} catch (IOException e) {
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[] { 6, BatchNo });
			jdbcTemplate.update(
					"INSERT INTO orderlog (batchno,front,behind,note,dealtime) VALUES (?,5,6,'发送保险网关平台失败1002',now())",
					new Object[] { BatchNo });
			return "err:发送保险网关平台失败1002";
		}
		return "0000";
	}
}
