package com.gopay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

import com.gus.EServerManage;
import com.gus.EServerManageService;


public class ZJbx {

	public static String zjbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) {

		List<Map<String, Object>> info = jdbcTemplate.queryForList(
				"SELECT interuser,interpwd,CODE FROM insproject WHERE id = ? ", new Object[] { projectid });
		if (info == null || info.size() == 0) {

			return "方案号错误";
		}

		String idnum = "";

		// 紫金保险
		final StringBuffer buff_xml = new StringBuffer();
		// ===========================head===============================
		buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buff_xml.append("<Packet type=\"REQUEST\" version=\"1.0\">");

		List<Map<String, Object>> idparents = jdbcTemplate.queryForList(
				"SELECT idparent FROM inskind,picc_teamorder WHERE inskind.id = picc_teamorder.kindid AND  batchno = ? ",
				new Object[] { BatchNo });
		if (idparents == null || idparents.size() == 0) {
			return "TransType 错误";
		}

		String tp = "J0103";

		String idp = idparents.get(0).get("idparent") + "";

		// J0101
		if ("3".equals(idp)) {
			tp = "J0104";
		}
		
		if("2".equals(idp)){
			tp = "J0103";
		}
		String PdfType = "";

		if ("1".equals(idp)) {
			PdfType = "0798B";
		} else if ("2".equals(idp)) {
			PdfType = "0798H";
		} else if ("3".equals(idp)) {
			PdfType = "0798A";
		} else {
			return "PdfType 错误";
		}

		buff_xml.append("<Head><TransType>" + tp + "</TransType>");
		String tid = "zj" + System.currentTimeMillis();
		buff_xml.append("<TransID>" + tid + "</TransID>");
		Date d = new Date();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String orderDatetime = s.format(d);
		buff_xml.append("<TransDate>" + orderDatetime + "</TransDate>");
		buff_xml.append("<RequestType>02</RequestType>");
		// buff_xml.append("<User>"+info.get(0).get("interuser")+"</User>");
		// buff_xml.append("<Password>"+info.get(0).get("interpwd")+"</Password>");

		buff_xml.append("<User>" + info.get(0).get("interuser") + "</User>");
		buff_xml.append("<Password>" + info.get(0).get("interpwd") + "</Password>");

		buff_xml.append("</Head>");
		// ===========================head===============================

		buff_xml.append("<Body>");

		// ===================================BasePart==========================================
		buff_xml.append("<BasePart>");

		String tbr = "";

		List<Map<String, Object>> order_res = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamorder WHERE picc_teamorder.BatchNo = ? ", new Object[] { BatchNo });

		if (order_res == null || order_res.size() == 0) {
			return "订单错误,无此订单记录";
		}

		tbr = order_res.get(0).get("LinkMan1") + "";

		buff_xml.append("<ProposalNo></ProposalNo>");

		buff_xml.append("<OrderNo>" + BatchNo + "</OrderNo>");

		buff_xml.append("<BusinessNature>2</BusinessNature>");

		buff_xml.append("<BusinessType>02</BusinessType>");

		buff_xml.append("<Category>" + order_res.get(0).get("RiskWrapCode") + "</Category>");

		buff_xml.append("<SumAmount>" + order_res.get(0).get("Amnt") + "</SumAmount>");

		buff_xml.append("<SumPremium>" + order_res.get(0).get("realprem") + "</SumPremium>");

		buff_xml.append("<SumQuantity>" + order_res.get(0).get("PeoplesInsu") + "</SumQuantity>");

		int sumQuantity = Integer.valueOf(order_res.get(0).get("PeoplesInsu") + "");

		buff_xml.append("<RiskCode>" + info.get(0).get("CODE") + "</RiskCode>");

		buff_xml.append("<GoalInsuredFlag>0</GoalInsuredFlag>");

		buff_xml.append("<AutoTransRenewFlag>99</AutoTransRenewFlag>");

		buff_xml.append("<ShareHolderFlag>0</ShareHolderFlag>");

		buff_xml.append("<ArgueSolution>2</ArgueSolution>");

		buff_xml.append("<ArbitBoardName></ArbitBoardName>");

		buff_xml.append("<StartDate>" + order_res.get(0).get("CValiDate").toString().replace("00:00:00.0", "")
				+ "</StartDate>");

		// buff_xml.append("<StartDate>2016-1-20</StartDate>");

		buff_xml.append("<StartHour>0</StartHour>");

		buff_xml.append(
				"<EndDate>" + order_res.get(0).get("CInValiDate").toString().replace("00:00:00.0", "") + "</EndDate>");

		// buff_xml.append("<EndDate>2016-1-30</EndDate>");

		buff_xml.append("<EndHour>24</EndHour>");

		buff_xml.append("<InputDate>" + order_res.get(0).get("HandlerDate").toString().replace("00:00:00.0", "")
				+ "</InputDate>");

		// 业务归属员
		List<Map<String, Object>> agentcode_list = jdbcTemplate.queryForList(
				"SELECT picc_teamorder.AgentCode FROM picc_teamorder WHERE BatchNo = ? ", new Object[] { BatchNo });

		List<Map<String, Object>> comcode_list = jdbcTemplate.queryForList(
				"SELECT picc_teamorder.EjbAgent FROM  picc_teamorder WHERE BatchNo = ? ", new Object[] { BatchNo });

		List<Map<String, Object>> handler1Code_list = jdbcTemplate.queryForList(
				"SELECT picc_teamorder.AgentCom  FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });

		if (agentcode_list == null || agentcode_list.size() == 0) {
			return "OperatorCode 错误";
		}

		if (comcode_list == null || comcode_list.size() == 0) {
			return "ComCode 错误";
		}

		if (handler1Code_list == null || handler1Code_list.size() == 0) {
			return "handler1Code 错误";
		}

		buff_xml.append("<OperatorCode>" + agentcode_list.get(0).get("AgentCode") + "</OperatorCode>");

		// buff_xml.append("<OperatorCode>211003024</OperatorCode>");

		// buff_xml.append("<MakeCom>211003024</MakeCom>");

		// =======FIXME======2320100501
		// buff_xml.append("<ComCode>" + (order_res.get(0).get("AgentCom"))
		// + "</ComCode>");

		// ===================2114200000
		buff_xml.append("<ComCode>" + comcode_list.get(0).get("EjbAgent") + "</ComCode>");
		buff_xml.append("<MakeCom>" + comcode_list.get(0).get("EjbAgent") + "</MakeCom>");

		// buff_xml.append("<ComCode>2114200000</ComCode>");

		// buff_xml.append("<Handler1Code>"
		// + order_res.get(0).get("AgentCode") + "</Handler1Code>");

		buff_xml.append("<Handler1Code>" + agentcode_list.get(0).get("AgentCode") + "</Handler1Code>");

		// buff_xml.append("<Handler1Code>211003024</Handler1Code>");

		buff_xml.append("<PhoneType>13</PhoneType>");

		// buff_xml.append("<DisRate></DisRate>");
		//
		// buff_xml.append("<AgentCode></AgentCode>");
		//
		// buff_xml.append("<AgreementNo>U200002000065-01</AgreementNo>");
		buff_xml.append("<AgreementNo>" + handler1Code_list.get(0).get("AgentCom") + "</AgreementNo>");

		buff_xml.append("<PdfType>" + PdfType + "</PdfType>");

		List<Map<String, Object>> p1 = jdbcTemplate.queryForList("SELECT interkey FROM insproject WHERE  id = ?",
				new Object[] { projectid });

		String pp = (String) p1.get(0).get("interkey");

		if (pp == null) {
			buff_xml.append("<ProductNo/>");
		} else {
			buff_xml.append("<ProductNo>" + pp + "</ProductNo>");
		}

		buff_xml.append("</BasePart>");

		// ===================================BasePart==========================================

		// ===================================InsuredList=======================================

		buff_xml.append("<InsuredList>");

		List<Map<String, Object>> picc_teamperson_res = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
				new Object[] { BatchNo });
		if (picc_teamperson_res == null || picc_teamperson_res.size() == 0) {
			return "err:投保人信息为空";
		}

		for (Iterator<Map<String, Object>> iterator = picc_teamperson_res.iterator(); iterator.hasNext();) {

			Map<String, Object> map = iterator.next();

			// =================================投保人===================================

			buff_xml.append("<InsuredData>");

			buff_xml.append("<InsuredType>1</InsuredType>");

			buff_xml.append("<InsuredFlag>2</InsuredFlag>");

			// 对应关系自己 01
			buff_xml.append("<InsuredIdentity>01</InsuredIdentity>");

			buff_xml.append("<InsuredNature>3</InsuredNature>");

			Map<String, Object> m = jdbcTemplate.queryForMap(
					"SELECT agentcode FROM toinscompany WHERE insid=7 AND kind=6 AND syscode= ?",
					new Object[] { map.get("IDType") });

			buff_xml.append("<IdentifyType>" + m.get("agentcode") + "</IdentifyType>");

			idnum = (String) map.get("IDNo");
			buff_xml.append("<IdentifyNumber>" + map.get("IDNo") + "</IdentifyNumber>");

			// buff_xml.append("<ValidPeriod3></ValidPeriod3>");

			buff_xml.append("<InsuredName>" + map.get("InsuredName") + "</InsuredName>");

			buff_xml.append("<Sex>" + ((Integer) map.get("Sex") + 1) + "</Sex>");

			Calendar cal = Calendar.getInstance();

			buff_xml.append("<Age>"
					+ (cal.get(Calendar.YEAR) - Integer.parseInt(map.get("birthday").toString().substring(0, 4)))
					+ "</Age>");

			// buff_xml.append("<Nationality></Nationality>");

			buff_xml.append("<Occupation>" + map.get("personid") + "</Occupation>");

			buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");

			// buff_xml.append("<SalfPhoneNumber></SalfPhoneNumber>");

			if (map.get("Phone") != null) {
				buff_xml.append("<SalfMobile>" + map.get("Phone") + "</SalfMobile>");
				buff_xml.append("<Mobile>" + map.get("Phone") + "</Mobile>");
			}

			buff_xml.append("<LinkerName>" + tbr + "</LinkerName>");

			// buff_xml.append("<SalfPostAddress>"+map.get("GrpAddress")+"</SalfPostAddress>");

			buff_xml.append("<RiskLevel>4</RiskLevel>");

			buff_xml.append("<Reserved2>4</Reserved2>");

			// List<Map<String, Object>> p1 = jdbcTemplate.queryForList(
			// "SELECT interkey FROM insproject WHERE id = ?",
			// new Object[] { projectid });

			// String pp = (String) p1.get(0).get("interkey");

			buff_xml.append("<ProductNo>" + pp + "</ProductNo>");

			buff_xml.append("</InsuredData>");

			break;
		}

		// =========================================被保人====================================================

		List<Map<String, Object>> picc_teamperson_bb = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });

		// System.out.println(BatchNo+"--"+picc_teamperson_bb.size());

		for (Map<String, Object> map : picc_teamperson_bb) {

			buff_xml.append("<InsuredData>");

			buff_xml.append("<InsuredType>1</InsuredType>");

			buff_xml.append("<InsuredFlag>1</InsuredFlag>");

			// Map m1 = jdbcTemplate.queryForMap(
			// "Select agentcode from toinscompany where insid=7 and kind=5 and
			// syscode= ?",
			// new Object[] { map.get("RelationToInsured") });

			// 对应关系自己 01
			buff_xml.append("<InsuredIdentity>" + map.get("RelationToInsured") + "</InsuredIdentity>");

			buff_xml.append("<InsuredNature>3</InsuredNature>");

			Map<String, Object> m = jdbcTemplate.queryForMap(
					"SELECT agentcode FROM toinscompany WHERE insid=7 AND kind=6 AND syscode= ?",
					new Object[] { map.get("IDType") });

			buff_xml.append("<IdentifyType>" + m.get("agentcode") + "</IdentifyType>");

			buff_xml.append("<IdentifyNumber>" + map.get("IDNo") + "</IdentifyNumber>");

			// buff_xml.append("<ValidPeriod3></ValidPeriod3>");

			buff_xml.append("<InsuredName>" + map.get("InsuredName") + "</InsuredName>");

			buff_xml.append("<Sex>" + ((Integer) map.get("Sex") + 1) + "</Sex>");

			Calendar cal = Calendar.getInstance();

			buff_xml.append("<Age>"
					+ (cal.get(Calendar.YEAR) - Integer.parseInt(map.get("birthday").toString().substring(0, 4)))
					+ "</Age>");

			// buff_xml.append("<Nationality></Nationality>");

			buff_xml.append("<Occupation>" + map.get("personid") + "</Occupation>");

			buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");

			// buff_xml.append("<SalfPhoneNumber></SalfPhoneNumber>");

			if (map.get("Phone") == null) {
				buff_xml.append("<SalfMobile></SalfMobile>");
			} else {
				buff_xml.append("<SalfMobile>" + map.get("Phone") + "</SalfMobile>");
			}

			// buff_xml.append("<SalfPostAddress>"+map.get("GrpAddress")+"</SalfPostAddress>");

			buff_xml.append("<RiskLevel>4</RiskLevel>");

			buff_xml.append("<Reserved2>4</Reserved2>");

			// List<Map<String, Object>> p1 = jdbcTemplate.queryForList(
			// "SELECT interkey FROM insproject WHERE id = ?",
			// new Object[] { projectid });

			// Integer pid = (Integer) map.get("personid");

			pp = (String) p1.get(0).get("interkey");

			if (p1 != null) {
				buff_xml.append("<ProductNo>" + pp + "</ProductNo>");
			}

			List<Map<String, Object>> p2 = jdbcTemplate.queryForList(
					"SELECT IFNULL(ROUND(picc_teamproduct.FacValAmnt/insproduct.reward),1) pn  FROM picc_teamproduct,insproduct WHERE  picc_teamproduct.productid = insproduct .id AND picc_teamproduct.ismaster=1 AND picc_teamproduct.BatchNo = ? limit 0 ,1",
					new Object[] { BatchNo });
			if (p2 != null && p2.size() > 0) {
				buff_xml.append("<ProductQuantity>" + ((Double) p2.get(0).get("pn")).intValue() + "</ProductQuantity>");
			}

			List<Map<String, Object>> p3 = jdbcTemplate.queryForList(
					" SELECT IFNULL(SUM(picc_teamproduct.realprem),0) ipn FROM picc_teamproduct WHERE BatchNo = ?",
					new Object[] { BatchNo });
			if (p3 != null && p3.size() > 0) {
				buff_xml.append("<InsuredPremium>" + ((Double) p3.get(0).get("ipn")).intValue() + "</InsuredPremium>");
			}

			buff_xml.append("</InsuredData>");

		}

		buff_xml.append("</InsuredList>");

		// ===============================险别信息===========================
		buff_xml.append("<ItemKindList>");

		for (Map<String, Object> map : picc_teamperson_bb) {

			List<Map<String, Object>> picc_product_info = jdbcTemplate.queryForList(
					"SELECT FacValAmnt,productid,projectid,RiskWrapCode,RiskCode,FacValAmnt/realprem Rate,realprem FROM picc_teamproduct WHERE BatchNo  = ? and personid = ?",
					new Object[] { BatchNo, map.get("personid") });

			for (Map<String, Object> map2 : picc_product_info) {

				buff_xml.append("<ItemKindData>");

				buff_xml.append("<FamilyName>" + map.get("InsuredName") + "</FamilyName>");
				buff_xml.append("<Occupation>" + map.get("personid") + "</Occupation>");

				buff_xml.append("<IdentifyNumber>" + map.get("IDNo") + "</IdentifyNumber>");

				buff_xml.append("<KindCode>" + map2.get("RiskCode") + "</KindCode>");

				List<Map<String, Object>> m3;
				if (pp == null) {
					m3 = jdbcTemplate.queryForList(
							"Select name,acode,aname from insproduct where id = ? and companyid = ? ",
							new Object[] { map2.get("productid"), companyid });
				} else {
					m3 = jdbcTemplate.queryForList(
							"Select name,acode,aname from insproduct where id = ? and companyid = ? and note = ? ",
							new Object[] { map2.get("productid"), companyid, pp });
				}

				//System.out.println(map2.get("productid")+"--"+ companyid+"--"+pp);
				
				buff_xml.append("<KindName>" + m3.get(0).get("name") + "</KindName>");

				if (m3.get(0).get("acode") != null) {
					buff_xml.append("<ItemCode>" + m3.get(0).get("acode") + "</ItemCode>");
				}

				if (m3.get(0).get("aname") != null) {
					buff_xml.append("<ItemDetailName>" + m3.get(0).get("aname") + "</ItemDetailName>");
				}

				// buff_xml.append("<ItemCode>"+map2.get("RiskCode")+"</ItemCode>");

				// Map<String, Object> m4 = jdbcTemplate.queryForMap(
				// "Select unitamount from insproduct where id= ?",
				// new Object[] { map2.get("productid") });

				// *******************
				if (idp.equals("3")) {
					buff_xml.append("<UnitAmount>" + (Float.valueOf(map2.get("FacValAmnt") + "") * sumQuantity)
							+ "</UnitAmount>");
				} else {
					buff_xml.append("<UnitAmount>" + map2.get("FacValAmnt") + "</UnitAmount>");
				}

				buff_xml.append("<Currency>CNY</Currency>");

				buff_xml.append("<Rate>" + (map2.get("Rate") == null ? 0 : map2.get("Rate")) + "</Rate>");

				buff_xml.append("<Discount>100</Discount>");

				buff_xml.append("<UnitPremium>" + map2.get("realprem") + "</UnitPremium>");

				buff_xml.append("<ShortRateFlag>3</ShortRateFlag>");

				buff_xml.append("<ShortRate>100</ShortRate>");

				buff_xml.append("</ItemKindData>");
			}
		}

		buff_xml.append("</ItemKindList>");

		// =============================缴费计划==================================
		buff_xml.append("<PlanList>");

		List<Map<String, Object>> picc_plan_info = jdbcTemplate
				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo  = ?", new Object[] { BatchNo });

		for (Map<String, Object> map3 : picc_plan_info) {

			buff_xml.append("<PlanData>");

			buff_xml.append("<SerialNo>1</SerialNo>");

			buff_xml.append("<PlanStartDate>" + map3.get("CValiDate").toString().replace("00:00:00.0", "")
					+ "</PlanStartDate>");

			buff_xml.append(
					"<PlanDate>" + map3.get("CInValiDate").toString().replace("00:00:00.0", "") + "</PlanDate>");

			// buff_xml.append("<PlanStartDate>2016-1-20</PlanStartDate>");
			//
			// buff_xml.append("<PlanDate>2016-1-30</PlanDate>");

			buff_xml.append("<Curreny>CNY</Curreny>");

			buff_xml.append("<PlanFee>" + map3.get("Prem") + "</PlanFee>");

			buff_xml.append("</PlanData>");
		}

		buff_xml.append("</PlanList>");

		// =======================================================Engage对象详细信息==============

		buff_xml.append("<EngageList>");

		// List<Map<String, Object>> picc_engage_info = jdbcTemplate
		// .queryForList("SELECT Remark FROM picc_teamorder WHERE BatchNo = ?",
		// new Object[] { BatchNo });
		//
		// if (picc_engage_info == null || picc_engage_info.size() == 0) {

		buff_xml.append("<EngageData>");

		buff_xml.append("<ClauseCode>T9999</ClauseCode>");

		buff_xml.append("<ClauseName>其它</ClauseName>");

		String msg = "";

		if ("1".equals(idp)) {
			msg = "	1、被保险人及职业类别以保单所附的被保险人清单为准。<br/>" + "2、无其他特别约定,未尽事宜以条款为准。";
		} else if ("2".equals(idp)) {
			msg = "1、被保险人及职业类别以保单所附的被保险人清单为准。<br/>" + "2、无其他特别约定,未尽事宜以条款为准。";
		} else if ("3".equals(idp)) {
			msg = "1、本保单被保险人年龄范围：1-75岁周岁（含）；被保险人若超出该年龄段不属于保险责任范畴。<br/>" + "2、本保单的被保险人为驾驶或乘坐保险单载明车辆的人员。<br/>"
					+ "3、驾驶员需持有效驾照；投保车辆必须符合国家规定的标准，具备合法有效的行驶证和年检证明。<br/>"
					+ "4、保单严格按照车辆行驶核定的座位数承保，每位驾/乘人员的身故残疾保险金额、意外医疗保险金额和意外伤害住院津贴以保单列明为限；意外伤害住院津贴最高以180天为限；意外伤害医疗保险金在扣除500元后按照90%进行赔付。<br/>"
					+ "5、18周岁以下未成年人的意外身故最高给付金额按照中国保监会相关规定执行。<br/>" + "6、若车主意外身故或残疾保额全额支付，或车辆报废或转手，本保单失效。<br/>"
					+ "7、无论您持有几份本保单，同一保险期限内保险人对同一被保险人所承担的保险金给付责任以一份为限，超过部分无效。<br/>"
					+ "8、本保单适用于紫金财产保险股份有限公司《紫金驾乘人员人身意外伤害保险条款》、《附加团体意外伤害住院安心保险条款》。";
		}

		buff_xml.append("<Clauses>" + msg + "</Clauses>");

		buff_xml.append("</EngageData>");
		// } else {
		// for (Map<String, Object> map4 : picc_engage_info) {
		//
		// buff_xml.append("<EngageData>");
		//
		// buff_xml.append("<ClauseCode>T9999</ClauseCode>");
		//
		// buff_xml.append("<ClauseName>其它</ClauseName>");
		//
		// buff_xml.append(
		// "<Clauses>" + (map4.get("Remark") == null ||
		// map4.get("Remark").toString().trim().equals("")
		// ? "no" : map4.get("Remark")) + "</Clauses>");
		//
		// buff_xml.append("</EngageData>");
		//
		// }
		// }

		buff_xml.append("</EngageList>");

		// XXX 2016 4.24
		List<Map<String, Object>> picc_car_info = jdbcTemplate
				.queryForList("SELECT * FROM picc_carperson WHERE BatchNo  = ?", new Object[] { BatchNo });
		if (picc_car_info != null && picc_car_info.size() > 0) {
			buff_xml.append("<CarInfoData>");

			buff_xml.append("<CarOwner>" + picc_car_info.get(0).get("cmaster") + "</CarOwner>");

			buff_xml.append("<LicenseNo>" + picc_car_info.get(0).get("carnumber") + "</LicenseNo>");
			buff_xml.append("<VinNo>" + picc_car_info.get(0).get("framenumber") + "</VinNo>");
			buff_xml.append("<UseNatureCode>" + picc_car_info.get(0).get("usekind") + "</UseNatureCode>");
			String cid = (String) picc_car_info.get(0).get("carkind");
			buff_xml.append("<CarKindCode>" + (cid.length() == 2 ? cid : cid.substring(0, 2)) + "</CarKindCode>");
			buff_xml.append("<SeatCount>" + picc_car_info.get(0).get("seatnum") + "</SeatCount>");
			buff_xml.append("<TonCount>" + (cid.length() == 2 ? "1" : cid.substring(2, 3)) + "</TonCount>");

			List<Map<String, Object>> picc_plan1_info = jdbcTemplate.queryForList(
					"SELECT insproject.interkey FROM insproject WHERE id = ?", new Object[] { projectid });

			buff_xml.append("<PlanNo>" + picc_plan1_info.get(0).get("interkey") + "</PlanNo>");

			buff_xml.append("</CarInfoData>");
		}

		// buff_xml.append("<CarInfoData>" + "<CarOwner>test</CarOwner>" +
		// "<LicenseNo>苏A123456</LicenseNo>"
		// + "<VinNo>AAAAAAA</VinNo>" + "<UseNatureCode>85</UseNatureCode>"
		// + "<CarKindCode>A0</CarKindCode>" + "<SeatCount>5</SeatCount>"
		// + "<TonCount>2</TonCount>" + "<PlanNo>A</PlanNo></CarInfoData>");

		buff_xml.append("</Body>");

		buff_xml.append("</Packet>");

		System.out.println(buff_xml);

		//GeneratePolicyService
		EServerManageService ems = new EServerManageService();
		EServerManage em = ems.getPort(EServerManage.class);

		String s1 = em.getServerXML(buff_xml.toString());

		System.out.println(idnum + "==============");
		System.out.println(s1 + "---");

		if (s1.indexOf("<ErrorCode>0000</ErrorCode>") == -1) {
			Pattern p = Pattern.compile("<ErrorMessage>(.*)</ErrorMessage>");
			Matcher m = p.matcher(s1);
			// jdbcTemplate
			// .update("INSERT INTO orderlog
			// (batchno,front,behind,note,dealtime,companyid) VALUES
			// (?,5,6,'出单失败',now(),?)",
			// new Object[] { BatchNo, companyid });

			while (m.find()) {
				jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
						new Object[] { 6, m.group(1), BatchNo });
				return "出单失败：" + m.group(1);
			}
		}

		Pattern p = Pattern.compile("<PolicyNo>(.*)</PolicyNo>");
		Matcher m = p.matcher(s1);

		while (m.find()) {
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,GrpContNo = ? WHERE batchno = ?",
					new Object[] { 7, m.group(1), BatchNo });

			String downurl = "http://www.10108080.cn/web/epolicy/downLoadFZEpolicy.do?policyNo=" + m.group(1)
					+ "&IdentifyNumber=" + idnum;

			// String downurl =
			// "http://221.226.62.39:7002/web/epolicy/downLoadFZEpolicy.do?policyNo="
			// + m.group(1) + "&IdentifyNumber=" + idnum;

			System.out.println(downurl + "--------" + BatchNo);

			jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? WHERE batchno = ?",
					new Object[] { downurl, BatchNo });

		}
		// jdbcTemplate
		// .update("INSERT INTO orderlog
		// (batchno,front,behind,note,dealtime,companyid) VALUES
		// (?,5,7,'出单成功',now(),?)",
		// new Object[] { BatchNo, companyid });

		return "0000";
	}

	public static void main(String[] args) {
		// String s1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Packet
		// type=\"REQUEST\"
		// version=\"1.0\"><Head><TransType>J0103</TransType><TransID>zj1458699847407</TransID><RequestType>02</RequestType><TransDate></TransDate><ErrorCode>0000</ErrorCode><ErrorMessage>成功</ErrorMessage><User>BYWL_PROD</User><Password>geodf64332</Password></Head><Body><ProposalNo>10798232013916000020</ProposalNo><PolicyNo>20798232013916000020</PolicyNo><OrderNo>BYW2016032310213644968</OrderNo></Body>";
		// Pattern p = Pattern.compile("<PolicyNo>(.*)</PolicyNo>");
		// Matcher m = p.matcher(s1);
		// while (m.find()) {
		// System.out.println(m.group(1));
		//
		// }

		System.out.println("123".length() == 2 ? "123" : "123".substring(2, 3));
	}
}
