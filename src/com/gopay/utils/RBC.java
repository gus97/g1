package com.gopay.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gus.db.MD5;
import com.gus.rbczs.EcooperationWebService;
import com.gus.rbczs.EcooperationWebServiceService;
//import com.gus.rbc.print.PrintWebService;
//import com.gus.rbc.print.PrintWebServiceService;

public class RBC {

	@SuppressWarnings("unused")
	public static String rbc(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) {

		List<Map<String, Object>> info = jdbcTemplate.queryForList(
				"SELECT interuser,interpwd,CODE,NAME FROM insproject WHERE id = ? ", new Object[] { projectid });

		if (info == null || info.size() == 0) {

			return "方案号错误";
		}

		List<Map<String, Object>> teamorderList = jdbcTemplate
				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });

		Date d = new Date();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat r = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat t = new SimpleDateFormat("HH:mm:ss");
		String r1 = r.format(d);
		String t1 = t.format(d);
		String s1 = s.format(d);

		final StringBuffer buff_xml = new StringBuffer();

		buff_xml.append("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?>");
		buff_xml.append("<ApplyInfo>");
		buff_xml.append("<GeneralInfo>");
		buff_xml.append("<UUID>" + BatchNo + "</UUID>");

		if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 11 AND kind=1") == 0) {
			return "AgentCom error";
		}

		Map<String, Object> m = jdbcTemplate.queryForMap(
				"SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND insid = 11 AND kind=1",
				new Object[] { medid });

		buff_xml.append("<PlateformCode>" + m.get("agentcode") + "</PlateformCode>");
		// Picc37mu63ht38mw
		buff_xml.append("<Md5Value>"
				+ MD5.md5(BatchNo + teamorderList.get(0).get("Prem").toString() + "Picc89MK8yjq6h7j") + "</Md5Value>");
		buff_xml.append("</GeneralInfo>");

		// ==============================================================================

		int cnt = jdbcTemplate.queryForInt(
				"SELECT count(1) FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });

		buff_xml.append("<PolicyInfos>");
		buff_xml.append("<PolicyInfo>");
		buff_xml.append("<SerialNo>1</SerialNo>");
		buff_xml.append("<RiskCode>EAC</RiskCode>");
		buff_xml.append("<OperateTimes>" + teamorderList.get(0).get("SendTime").toString().substring(0, 19)
				+ "</OperateTimes>");
		buff_xml.append(
				"<StartDate>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10) + "</StartDate>");
		buff_xml.append(
				"<EndDate>" + teamorderList.get(0).get("CInValiDate").toString().substring(0, 10) + "</EndDate>");
		buff_xml.append("<StartHour>0</StartHour>");
		buff_xml.append("<EndHour>24</EndHour>");
		buff_xml.append(
				"<SumAmount>" + Double.parseDouble(teamorderList.get(0).get("Amnt").toString()) + "</SumAmount>");
		buff_xml.append("<SumPremium>" + teamorderList.get(0).get("realprem").toString() + "</SumPremium>");
		buff_xml.append("<ArguSolution>1</ArguSolution>");
		buff_xml.append("<Quantity>1</Quantity>");
		// ================================================================================
		buff_xml.append("<InsuredPlan>");
		// buff_xml.append("<RationType>EAC0000001</RationType>");

		buff_xml.append("<RationType>" + info.get(0).get("CODE") + "</RationType>");

		// ================================================================================
		List<Map<String, Object>> picc_product_info = jdbcTemplate.queryForList(
				"SELECT FacValAmnt,FacValAmntType,productid,projectid,RiskWrapCode,RiskCode,FacValAmnt/realprem Rate,realprem,Prem FROM picc_teamproduct WHERE BatchNo  = ?",
				new Object[] { BatchNo });
		buff_xml.append("<Schemes>");

		String sr = "";

		String sr1 = "";

		for (Map<String, Object> map : picc_product_info) {
			buff_xml.append("<Scheme>");

			sr = (String) map.get("RiskCode");

			if (sr.startsWith("2_80")) {
				sr = "2";
				sr1 = "2_80";
			} else if (sr.startsWith("2_100")) {
				sr = "2";
				sr1 = "2_100";
			}

			buff_xml.append("<SchemeCode>" + sr + "</SchemeCode>");

			buff_xml.append(
					"<SchemeAmount>" + Double.parseDouble(map.get("FacValAmnt").toString()) * cnt + "</SchemeAmount>");

			buff_xml.append(
					"<SchemePremium>" + Double.parseDouble(map.get("realprem").toString()) * cnt + "</SchemePremium>");
			buff_xml.append("</Scheme>");
		}
		buff_xml.append("</Schemes>");
		buff_xml.append("</InsuredPlan>");
		// ==================================================================================
		buff_xml.append("<Applicant>");
		List<Map<String, Object>> picc_teamperson_res = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
				new Object[] { BatchNo });
		if (picc_teamperson_res == null || picc_teamperson_res.size() == 0) {
			return "err:投保人信息为空";
		}
		buff_xml.append("<AppliName>" + picc_teamperson_res.get(0).get("InsuredName") + "</AppliName>");
		String AppliIdType = picc_teamperson_res.get(0).get("IDType").toString();
		if ("0".equals(AppliIdType)) {
			AppliIdType = "01";
		} else if ("5".equals(AppliIdType)) {
			AppliIdType = "02";
		} else if ("1".equals(AppliIdType)) {
			AppliIdType = "03";
		} else if ("2".equals(AppliIdType)) {
			AppliIdType = "04";
		} else if ("3".equals(AppliIdType)) {
			AppliIdType = "05";
		} else if ("71".equals(AppliIdType)) {
			AppliIdType = "31";
		} else if ("95".equals(AppliIdType)) {
			AppliIdType = "37";
		} else {
			AppliIdType = "99";
		}

		buff_xml.append("<AppliIdType>" + AppliIdType + "</AppliIdType>");
		buff_xml.append("<AppliIdNo>" + picc_teamperson_res.get(0).get("IDNo") + "</AppliIdNo>");

		String AppliIdentity = picc_teamperson_res.get(0).get("RelationToInsured").toString();
		if (AppliIdentity.equals("00")) {
			AppliIdentity = "0";
		}
		if (AppliIdentity.equals("04") || AppliIdentity.equals("05")) {
			AppliIdentity = "7";
		}
		if (AppliIdentity.equals("27")) {
			AppliIdentity = "7";
		} else {
			AppliIdentity = "7";
		}
		buff_xml.append("<AppliIdentity>" + AppliIdentity + "</AppliIdentity>");
		// buff_xml.append("<AppliIdentity>7</AppliIdentity>");
		buff_xml.append("<Businesssource>0302</Businesssource>");

		buff_xml.append("</Applicant>");
		// ====================================================================================\
		buff_xml.append("<Insureds>");

		List<Map<String, Object>> picc_teamperson_bb = jdbcTemplate.queryForList(
				"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });

		for (Map<String, Object> map : picc_teamperson_bb) {

			AppliIdType = map.get("IDType").toString();
			if ("0".equals(AppliIdType)) {
				AppliIdType = "01";
			} else if ("5".equals(AppliIdType)) {
				AppliIdType = "02";
			} else if ("1".equals(AppliIdType)) {
				AppliIdType = "03";
			} else if ("2".equals(AppliIdType)) {
				AppliIdType = "04";
			} else if ("3".equals(AppliIdType)) {
				AppliIdType = "05";
			} else if ("71".equals(AppliIdType)) {
				AppliIdType = "31";
			} else if ("95".equals(AppliIdType)) {
				AppliIdType = "37";
			} else {
				AppliIdType = "99";
			}

			String InsuredIdentity = map.get("RelationToInsured").toString();

			if (InsuredIdentity.equals("00")) {
				InsuredIdentity = "0";
			} else if (InsuredIdentity.equals("04") || AppliIdentity.equals("05")) {
				InsuredIdentity = "0";
			} else {
				InsuredIdentity = "0";
			}

			buff_xml.append("<Insured>");
			buff_xml.append("<InsuredSeqNo>" + map.get("InsuredID") + "</InsuredSeqNo>");
			buff_xml.append("<InsuredName>" + map.get("InsuredName") + "</InsuredName>");
			buff_xml.append("<InsuredIdentity>" + InsuredIdentity + "</InsuredIdentity>");

			buff_xml.append("<InsuredIdType>" + AppliIdType + "</InsuredIdType>");

			buff_xml.append("<InsuredIdNo>" + map.get("IDNo") + "</InsuredIdNo>");
			buff_xml.append("<InsuredBirthday>" + map.get("Birthday") + "</InsuredBirthday>");
			buff_xml.append("<InsuredSex>" + (Integer.parseInt(map.get("sex") + "") + 1) + "</InsuredSex>");
			// <Occupation>5</Occupation>
			/**
			 * 如Creditlevel=1，则Occupationcode=000101，Occupation=职员；
			 * 如Creditlevel=2，则Occupationcode=010102，Occupation=司机；
			 * 如Creditlevel=3，则Occupationcode=010203，Occupation=放牧人员；
			 * 如Creditlevel=4，则Occupationcode=050325，Occupation=引水人；
			 * 如Creditlevel=5，则Occupationcode=030205，Occupation=装运工人；
			 * 如Creditlevel=6，则Occupationcode=040507，Occupation=石油开采工人；
			 */
			String Creditlevel = map.get("personid") + "";
			String Occupationcode = "030203";
			String Occupation = "IT";

			if (Creditlevel.equals("1")) {
				Occupationcode = "000101";
				Occupation = "职员";
			} else if (Creditlevel.equals("2")) {
				Occupationcode = "000102";
				Occupation = "司机";
			} else if (Creditlevel.equals("3")) {
				Occupationcode = "010203";
				Occupation = "放牧人员";
			} else if (Creditlevel.equals("4")) {
				Occupationcode = "050325";
				Occupation = "引水人";
			} else if (Creditlevel.equals("5")) {
				Occupationcode = "030205";
				Occupation = "装运工人";
			} else if (Creditlevel.equals("6")) {
				Occupationcode = "040507";
				Occupation = "石油开采工人";
			}

			buff_xml.append("<Occupationcode>" + Occupationcode + "</Occupationcode>");
			buff_xml.append("<Creditlevel>" + map.get("personid") + "</Creditlevel>");
			buff_xml.append("<Occupation>" + Occupation + "</Occupation>");
			buff_xml.append("<InsuredSpouseunitphone>1</InsuredSpouseunitphone>");
			buff_xml.append("</Insured>");
		}
		buff_xml.append("</Insureds>");

		String li = "100";

		if (sr1.startsWith("2_80")) {
			li = "80";
		}
		buff_xml.append("<ExtendInfos><ExtendInfo key=\"limit\">" + li + "</ExtendInfo></ExtendInfos>");

		buff_xml.append("</PolicyInfo>");
		buff_xml.append("</PolicyInfos>");
		buff_xml.append("</ApplyInfo>");

		System.out.println(buff_xml);

		System.out.println();

		EcooperationWebServiceService ews = new EcooperationWebServiceService();
		EcooperationWebService ew = ews.getPort(EcooperationWebService.class);
		String res = ew.insureService("001001", buff_xml.toString());

		System.out.println(res);

		Pattern p = Pattern.compile("<PolicyNo>(.*)</PolicyNo>");
		Matcher mt = p.matcher(res);

		String PolicyNo = "";
		while (mt.find()) {
			PolicyNo = mt.group(1);
		}

		p = Pattern.compile("<DownloadUrl>(.*)</DownloadUrl>");
		mt = p.matcher(res);

		String down = "";
		while (mt.find()) {
			down = mt.group(1);
		}
		down = down.replace("<![CDATA[", "").replace("]]>", "");

		if (res.indexOf("<ErrorCode>00</ErrorCode>") >= 0 && PolicyNo.length() > 0) {

			jdbcTemplate.update(
					"UPDATE picc_teamorder SET STATUS = ? ,downurl = ? , PrtNo = ? ,GrpContNo= ? WHERE BatchNo = ?",
					new Object[] { 7, down, "", PolicyNo, BatchNo });
			return "0000";
		} else {
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
					new Object[] { 6, res, BatchNo });
			return "出单失败：" + res;
		}
	}

	public static String getDZBD(String BatchNo, JdbcTemplate jdbcTemplate1) {
		/**
		 * <?xml version="1.0" encoding="GB2312" standalone="yes"?> <PrintMain>
		 * <GeneralInfo> <UUID>公司名称缩写（拼音或英文）+yyyyMMddhhmmssyyy</UUID>
		 * //UUID或填流水号之类的唯一标识 <PlateformCode>平台项目标识</PlateformCode>
		 * <Md5Value>md5校验</Md5Value> </GeneralInfo> <PrintBody >
		 * <PolicyNo>保单号</PolicyNo> <InsuredName>被保险人姓名 </InsuredName>
		 * <InsuredIdNo>被保险人证件号 </InsuredIdNo> </PrintBody > </PrintMain
		 * 
		 */

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
		final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

		List<Map<String, Object>> res = jdbcTemplate.queryForList(
				"select InsuredName,IDNo from Picc_teamperson where BatchNo= ? and RelationToInsured <> '00'",
				new Object[] { BatchNo });
		List<Map<String, Object>> r1 = jdbcTemplate
				.queryForList("SELECT GrpContNo  FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });

		String bname = "";
		String bid = "";
		for (Map<String, Object> map : res) {
			bname += map.get("InsuredName") + ",";
		}
		for (Map<String, Object> map : res) {
			bid += map.get("IDNo") + ",";
		}

		bname = bname.substring(0, bname.length() - 1);
		bid = bid.substring(0, bid.length() - 1);

		final StringBuffer buff_xml = new StringBuffer();
		buff_xml.append("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?>");
		buff_xml.append("<PrintMain>");
		buff_xml.append("<GeneralInfo>");
		String uuid = "BYW" + System.currentTimeMillis();
		buff_xml.append("<UUID>" + uuid + "</UUID>");
		Map<String, Object> m = jdbcTemplate
				.queryForMap("SELECT agentcode FROM toinscompany WHERE insid = 11 AND kind=1 limit 1");

		buff_xml.append("<PlateformCode>" + m.get("agentcode") + "</PlateformCode>");
		buff_xml.append("<Md5Value>" + MD5.md5(uuid + m.get("agentcode") + r1.get(0).get("GrpContNo")) + "</Md5Value>");
		buff_xml.append("</GeneralInfo>");
		buff_xml.append("<PrintBody>");
		buff_xml.append("<PolicyNo>" + r1.get(0).get("GrpContNo") + "</PolicyNo>");
		buff_xml.append("<InsuredName>" + bname + " </InsuredName>");
		buff_xml.append("<InsuredIdNo>" + bid + "</InsuredIdNo>");
		buff_xml.append("</PrintBody>");
		buff_xml.append("</PrintMain>");

		System.out.println(buff_xml);

		// PrintWebServiceService pwss = new PrintWebServiceService();
		// PrintWebService pws = pwss.getPort(PrintWebService.class);
		// String s = pws.printPolicyService("001001", buff_xml.toString());
		// System.out.println(s + "------");

		return null;
	}

	public static void main(String[] args) {

		String s = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?><ApplyInfo><GeneralInfo><UUID>BYW2017051614341552028</UUID><PlateformCode>CPI000178</PlateformCode><Md5Value>0c23258da33af964b727feb8ab5a8638</Md5Value></GeneralInfo><PolicyInfos><PolicyInfo><SerialNo>1</SerialNo><RiskCode>EAC</RiskCode><OperateTimes>2017-05-16 14:34:15</OperateTimes><StartDate>2017-05-17</StartDate><EndDate>2018-05-16</EndDate><StartHour>0</StartHour><EndHour>24</EndHour><SumAmount>60000.0</SumAmount><SumPremium>30.0</SumPremium><ArguSolution>1</ArguSolution><Quantity>1</Quantity><InsuredPlan><RationType>EAC0000001</RationType><Schemes><Scheme><SchemeCode>1</SchemeCode><SchemeAmount>60000.0</SchemeAmount><SchemePremium>30.0</SchemePremium></Scheme></Schemes></InsuredPlan><Applicant><AppliName>测试4</AppliName><AppliIdType>71</AppliIdType><AppliIdNo>TEST-04</AppliIdNo><AppliIdentity>0</AppliIdentity></Applicant><Insureds><Insured><InsuredSeqNo>2</InsuredSeqNo><InsuredName>测试41</InsuredName><InsuredIdType>0</InsuredIdType><InsuredIdNo>320102198305012418</InsuredIdNo><InsuredBirthday>1983-05-01</InsuredBirthday><InsuredSex>1</InsuredSex></Insured><Insured><InsuredSeqNo>3</InsuredSeqNo><InsuredName>测试42</InsuredName><InsuredIdType>0</InsuredIdType><InsuredIdNo>320102198809202412</InsuredIdNo><InsuredBirthday>1988-09-20</InsuredBirthday><InsuredSex>1</InsuredSex></Insured></Insureds><ExtendInfos><ExtendInfo key=\"limit\">100</ExtendInfo></ExtendInfos></PolicyInfo></PolicyInfos></ApplyInfo>\n"
				+ "\n" + "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?>\n" + "<ReturnInfo>\n"
				+ "  <GeneralInfoReturn>\n" + "    <UUID>BYW2017051614341552028</UUID>\n"
				+ "    <PlateformCode>CPI000178</PlateformCode>\n" + "    <ErrorCode>00</ErrorCode>\n"
				+ "    <ErrorMessage>成功</ErrorMessage>\n" + "  </GeneralInfoReturn>\n" + "  <PolicyInfoReturns>\n"
				+ "    <PolicyInfoReturn>\n" + "      <SerialNo>1</SerialNo>\n"
				+ "      <PolicyNo>PEAC20173201Q000E00014</PolicyNo>\n" + "      <PolicyUrl></PolicyUrl>\n"
				+ "      <DownloadUrl><![CDATA[http://partnertest.mypicc.com.cn/ecooperation/policydownload/downloadurl.do?platfromcodes=CPI000178&policyNo=F64E36CF95F85A45941F66EACE6E7312B6B9C8EC1A9819646B68BEB2BA4DDB1D&insuredID=77DA6F0032A9F85368952281FF2A41B3C71ECB49B33DA0BBC94D794FA4029E0E&flag=N]]></DownloadUrl>\n"
				+ "      <SaveResult>00</SaveResult>\n" + "      <SaveMessage>处理成功</SaveMessage>\n"
				+ "      <SaveTimes>2017-05-16 14:35:20</SaveTimes>\n" + "      <InsuredReturns>\n"
				+ "        <InsuredReturn>\n" + "          <InsuredSeqNo>2</InsuredSeqNo>\n"
				+ "          <CheckResult>00</CheckResult>\n" + "          <CheckMessage>处理成功</CheckMessage>\n"
				+ "        </InsuredReturn>\n" + "        <InsuredReturn>\n"
				+ "          <InsuredSeqNo>3</InsuredSeqNo>\n" + "          <CheckResult>00</CheckResult>\n"
				+ "          <CheckMessage>处理成功</CheckMessage>\n" + "        </InsuredReturn>\n"
				+ "      </InsuredReturns>\n" + "    </PolicyInfoReturn>\n" + "  </PolicyInfoReturns>\n"
				+ "</ReturnInfo>\n";

		Pattern p = Pattern.compile("<DownloadUrl>(.*)</DownloadUrl>");
		Matcher mt = p.matcher(s);

		String s1 = "";
		while (mt.find()) {
			s1 = mt.group(1);
		}
		s1 = s1.replace("<![CDATA[", "").replace("]]>", "");
	}
}