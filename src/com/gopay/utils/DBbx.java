package com.gopay.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

import com.gus.db.CommServerWebService;
import com.gus.db.CommServerWebServicePortType;
import com.gus.db.MD5;

public class DBbx {

	public static String dbbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) {

		final StringBuffer buff_xml = new StringBuffer();
		// ===========================head===============================
		buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buff_xml.append("<request><header>");
		buff_xml.append("<msgId>srv0001</msgId>");
		Date d = new Date();
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssS");
		String timestamp = s.format(d);
		buff_xml.append("<timestamp>" + timestamp + "</timestamp>");
		buff_xml.append("<version>1.0</version>");
		buff_xml.append("<ipaddress>139.196.105.157</ipaddress>");
		buff_xml.append("<actionCode>1</actionCode>");
		buff_xml.append("</header>");
		// ===========================head===============================

		// ============================body===============================

		// ============================policyInfo=========================
		buff_xml.append("<body><policyInfo>");

		buff_xml.append("<applicationId>" + BatchNo + "</applicationId>");
		buff_xml.append("<serialNumber>" + BatchNo + "</serialNumber>");
		List<Map<String, Object>> planCodeList = jdbcTemplate
				.queryForList("select insproject.code  FROM insproject where id = ? ", new Object[] { projectid });

		buff_xml.append("<planCode>" + planCodeList.get(0).get("code") + "</planCode>");

		List<Map<String, Object>> teamorderList = jdbcTemplate
				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });

		buff_xml.append("<amount>" + teamorderList.get(0).get("Amnt") + "</amount>");

		buff_xml.append("<premium>" + teamorderList.get(0).get("realprem") + "</premium>");

		buff_xml.append(
				"<startDate>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10) + "</startDate>");

		buff_xml.append("<startHour>0</startHour>");

		buff_xml.append(
				"<endDate>" + teamorderList.get(0).get("CInValiDate").toString().substring(0, 10) + "</endDate>");

		buff_xml.append("<endHour>0</endHour>");

		buff_xml.append("<inputDate>" + timestamp + "</inputDate>");

		buff_xml.append("<noType></noType>");
		buff_xml.append("<noCode></noCode>");
		buff_xml.append("<travelFromAdress></travelFromAdress>");
		buff_xml.append("<travelToAdress></travelToAdress>");

		// System.out.println(medid + "====================");

		List<Map<String, Object>> agentCodeList = jdbcTemplate.queryForList(
				"SELECT toinscompany.agentcode FROM medcompany, toinscompany WHERE medcompany.id = ? AND toinscompany.kind = 1 AND toinscompany.insid = 8 AND toinscompany.syscode = medcompany.code",
				new Object[] { medid });

		buff_xml.append("<agentCode>" + agentCodeList.get(0).get("agentcode") + "</agentCode>");

		buff_xml.append("<coinsFlag>0</coinsFlag>");

		buff_xml.append("<mainSubPolicyNo></mainSubPolicyNo>");

		buff_xml.append("</policyInfo>");

		// =====================================<kindList>====================================================

		buff_xml.append("<kindList>");

		List<Map<String, Object>> kindList = jdbcTemplate.queryForList(
				"SELECT RiskCode,FacValAmnt,Prem,ReserveFee,realprem,scope FROM picc_teamproduct WHERE batchno = ?",
				new Object[] { BatchNo });

		DecimalFormat decimalFormat = new DecimalFormat("0.0000");

		for (Map<String, Object> map : kindList) {

			float sv = 1.0f;
			int scope = (Integer) (map.get("scope") == null ? 12 : map.get("scope"));
			if (scope == 1)
				sv = 0.2f;
			if (scope == 3)
				sv = 0.4f;
			if (scope == 6)
				sv = 0.7f;

			buff_xml.append("<kindItem>");
			buff_xml.append("<kindCode>" + map.get("RiskCode") + "</kindCode>");
			buff_xml.append("<kindAmount>" + map.get("FacValAmnt") + "</kindAmount>");
			buff_xml.append("<kindPremium>" + map.get("realprem") + "</kindPremium>");

			//ReserveFee
			buff_xml.append("<kindRate>" + decimalFormat.format(Float.parseFloat(map.get("realprem") + "") * 1000
					/ Float.parseFloat(map.get("FacValAmnt") + "") / sv) + "</kindRate>");
			buff_xml.append("</kindItem>");
		}
		buff_xml.append("</kindList>");

		List<Map<String, Object>> teampersonList = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
				new Object[] { BatchNo });

		if (teampersonList == null || teampersonList.size() == 0) {
			return "err:投保人信息为空";
		}

		buff_xml.append("<holder>");

		buff_xml.append("<name>" + teampersonList.get(0).get("InsuredName") + "</name>");

		int sex = (int) teampersonList.get(0).get("sex");

		buff_xml.append("<sex>" + (sex + 1) + "</sex>");

		buff_xml.append(
				" <birthday>" + teampersonList.get(0).get("Birthday").toString().replace("-", "") + "</birthday>");

		int idType = (int) teampersonList.get(0).get("IDType");

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

		buff_xml.append("<idType>" + st + "</idType>");

		buff_xml.append("<idNo>" + teampersonList.get(0).get("IDNo") + "</idNo>");

		// buff_xml.append("<idNo>320107198003061812</idNo>");

		String relaToInsured = (String) teampersonList.get(0).get("RelationToInsured");

		if ("00".equals(relaToInsured)) {
			relaToInsured = "01";
		} else if ("27".equals(relaToInsured)) {
			relaToInsured = "99";
		} else if ("26".equals(relaToInsured)) {
			relaToInsured = "80";
		} else if ("04".equals(relaToInsured)) {
			relaToInsured = "51";
		} else if ("05".equals(relaToInsured)) {
			relaToInsured = "52";
		}

		buff_xml.append("<relaToInsured>" + relaToInsured + "</relaToInsured>");

		String phone = (String) teampersonList.get(0).get("Phone");
		if (phone == null) {
			phone = "13912345678";
		}
		buff_xml.append("<mobile>" + phone + "</mobile>");
		buff_xml.append("<email></email>");
		buff_xml.append("</holder>");

		// ============================================<insureds>=============================
		buff_xml.append("<insureds>");

		List<Map<String, Object>> teampersonBBList = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });

		@SuppressWarnings("unused")
		Calendar a = Calendar.getInstance();
		//int year = a.get(Calendar.YEAR);

		for (Map<String, Object> map : teampersonBBList) {
			buff_xml.append("<insured>");
			buff_xml.append("<name>" + map.get("InsuredName") + "</name>");
			buff_xml.append("<birthday>" + map.get("Birthday").toString().replace("-", "") + "</birthday>");

			int idbType = (int) map.get("IDType");

			if (idbType == 0) {
				st = "01";
			} else if (idbType == 5) {
				st = "02";
			} else if (idbType == 1) {
				st = "03";
			} else if (idbType == 2) {
				st = "04";
			} else if (idbType == 3) {
				st = "05";
			} else {
				st = "99";
			}

			buff_xml.append("<idType>" + st + "</idType>");
			//
			buff_xml.append("<idNo>" + map.get("IDNo") + "</idNo>");

			// buff_xml.append("<idNo>320107198003061812</idNo>");

			sex = (int) map.get("sex");

			buff_xml.append("<sex>" + (sex + 1) + "</sex>");

			phone = (String) map.get("Phone");

			if (phone == null) {
				phone = "13912345678";
			}

			@SuppressWarnings("unused")
			String invoiceType = "";
			if (map.get("Birthday") != null) {
				invoiceType = map.get("Birthday").toString();
			}

//			int tp = Integer.parseInt(invoiceType.substring(0, 4));
//
//			if (year - tp < 18) {
//				tp = 3;
//			} else if (year - tp >= 18 && year - tp < 60) {
//				tp = 1;
//			} else {
//				tp = 2;
//			}
			
			int tp = 1;

			buff_xml.append("<mobile>" + phone + "</mobile>");

			buff_xml.append("<email></email>");

			buff_xml.append("<mult>1</mult>");

			buff_xml.append("<invoiceType>" + tp + "</invoiceType>");

			buff_xml.append("<amount></amount>");
			buff_xml.append("</insured>");
		}
		buff_xml.append("</insureds></body></request>");

		CommServerWebService ems = new CommServerWebService();
		CommServerWebServicePortType em = ems.getPort(CommServerWebServicePortType.class);

		System.out.println("承保：" + buff_xml);

		String es = MD5.md5("instony1234" + buff_xml.toString());
		String s1 = em.generatePolicyService(buff_xml.toString(), es);

		System.out.println("承保返回：" + s1);

		if (s1.indexOf("<resultcode>0</resultcode>") == -1) {
			Pattern p = Pattern.compile("<errordesc>(.*)</errordesc>");
			Matcher m = p.matcher(s1);

			while (m.find()) {
				jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
						new Object[] { 6, m.group(1), BatchNo });
				return "承保出单失败：" + m.group(1);
			}
		}

		Pattern p = Pattern.compile("<policyno>(.*)</policyno>");
		Matcher m = p.matcher(s1);

		String policyno = "";

		while (m.find()) {

			policyno = m.group(1);

			if (policyno.startsWith("9")) {

				jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,PrtNo = ? WHERE batchno = ?",
						new Object[] { 7, policyno, BatchNo });
			}

			// 承保确认
			final StringBuffer buff_xml_res = new StringBuffer();
			buff_xml_res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buff_xml_res.append("<request><header>");
			buff_xml_res.append("<version>1</version>");
			buff_xml_res.append("<msgId>srv0003</msgId>");
			Date d1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
			String timestamp1 = sdf.format(d1);
			buff_xml_res.append("<timestamp>" + timestamp1 + "</timestamp>");
			buff_xml_res.append("<ipaddress>139.196.105.157</ipaddress>");
			buff_xml_res.append("</header><body>");
			buff_xml_res.append("<subPolicyNo>" + m.group(1) + "</subPolicyNo>");
			buff_xml_res.append("</body></request>");

			System.out.println("承保确认：" + buff_xml_res);
			es = MD5.md5("instony1234" + buff_xml_res.toString());
			s1 = em.generatePolicyService(buff_xml_res.toString(), es);
			System.out.println("承保确认返回：" + s1);

			if (s1.indexOf("<resultcode>000000</resultcode>") == -1) {

				p = Pattern.compile("<errordesc>(.*)</errordesc>");
				m = p.matcher(s1);

				while (m.find()) {
					jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
							new Object[] { 6, m.group(1), BatchNo });
					return "承保确认出单失败：" + m.group(1);
				}
			}

			p = Pattern.compile("<policyno>(.*)</policyno>");
			m = p.matcher(s1);

			// 写入9开头的保单
			while (m.find()) {
				policyno = m.group(1);
			}

			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,GrpContNo = ? WHERE batchno = ?",
					new Object[] { 7, policyno, BatchNo });

			// http://59.151.126.66:8888/ecenter/epolicy/downloadFile.do?
			String downurl = "http://59.151.126.95/ecenter/epolicy/downloadFile.do?policyNo=" + policyno + "&md5="
					+ MD5.md5("instony1234" + policyno);

			System.out.println(downurl + "--------" + BatchNo + "--------" + policyno);

			jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? WHERE batchno = ?",
					new Object[] { downurl, BatchNo });
		}

		return "0000";
	}

	public static void main(String[] args) {
		// Date d = new Date();
		// SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssS");
		// String timestamp = s.format(d);
		// System.out.println(timestamp);

		Calendar a = Calendar.getInstance();
		System.out.println(a.get(Calendar.YEAR));

		System.out.println("2016-06-27 00:00:00.0".substring(0, 10));
		// GeneratePolicyService
		// EServerManageService ems = new EServerManageService();
		// EServerManage em = ems.getPort(EServerManage.class);
	}
}
