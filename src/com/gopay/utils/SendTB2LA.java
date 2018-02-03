package com.gopay.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.allinpay.S1;
import com.gus.EServerManage;
import com.gus.EServerManageService;
import com.gus.db.CommServerWebService;
import com.gus.db.CommServerWebServicePortType;
import com.gus.db.MD5;
import com.gus.rbczs.EcooperationWebService;
import com.gus.rbczs.EcooperationWebServiceService;


public class SendTB2LA extends HttpServlet {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger("op");
	private static final long serialVersionUID = -8918719111026075686L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	@SuppressWarnings("resource")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "No-Cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-Cache");
		request.setCharacterEncoding("UTF-8");
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=" + "UTF-8");
		//response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String BatchNo = request.getParameter("bno");
		String pid = request.getParameter("projectid");
		if (pid == null || !isNumeric(pid)) {
			out.print("projectid err");
			return;
		}
		Integer projectid = new Integer(pid);

		String cid = request.getParameter("companyid");
		if (cid == null || !isNumeric(cid)) {
			out.print("companyid err");
			return;
		}

		Integer companyid = new Integer(cid);

		String mid = request.getParameter("medid");
		
		if (mid == null || !isNumeric(mid)) {
			out.print("medid err");
			return;
		}

		// Integer medid = new Integer(mid);

		ApplicationContext context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
		final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

		if (companyid == 5) {

			final StringBuffer buff_xml = new StringBuffer();
			buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buff_xml.append("<PackageList><Package><Header>");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// ============================header===============================
			// UUID uuid = UUID.randomUUID();

			List<Map<String, Object>> pres = jdbcTemplate
					.queryForList("SELECT grpcontno FROM picc_teamorder WHERE batchno = ?", new Object[] { BatchNo });

			if (pres == null || pres.size() == 0) {
				out.print("SELECT grpcontno FROM picc_teamorder WHERE batchno = " + BatchNo + "执行结果为0");
				return;
			}

			// jdbcTemplate.update(
			// "UPDATE picc_teamorder SET note = ? WHERE batchno = ?",
			// new Object[] { uuid.toString(), BatchNo });

			buff_xml.append("<UUID></UUID>");
			String Time = sdf.format(new Date());
			buff_xml.append("<Time>" + Time + "</Time>");
			String Code = "30002";
			buff_xml.append("<Code>" + Code + "</Code>");
			String Asyn = "0";
			buff_xml.append("<Asyn>" + Asyn + "</Asyn>");
			String AsynReturnUrl = "";
			buff_xml.append("<AsynReturnUrl>" + AsynReturnUrl + "</AsynReturnUrl>");
			// 保险公司为渠道方分配
			String PartnerIdentifier = "hengsheng";
			buff_xml.append("<PartnerIdentifier>" + PartnerIdentifier + "</PartnerIdentifier>");
			buff_xml.append("<PartnerSerial></PartnerSerial>");
			buff_xml.append("</Header>");
			// ============================header===============================

			buff_xml.append("<Request><RefundList><RefundItem>");
			buff_xml.append("<PolicyNo>" + pres.get(0).get("grpcontno") + "</PolicyNo>");
			List<Map<String, Object>> picc_teamperson_res = jdbcTemplate.queryForList(
					"SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
					new Object[] { BatchNo });
			if (picc_teamperson_res == null || picc_teamperson_res.size() == 0) {
				out.print("SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = " + BatchNo
						+ " and RelationToInsured='00' 执行结果为0");
				return;
			}

			buff_xml.append("<ApplicantName>" + picc_teamperson_res.get(0).get("InsuredName") + "</ApplicantName>");
			buff_xml.append("<RefundTime>" + Time + "</RefundTime>");
			buff_xml.append("</RefundItem></RefundList></Request></Package></PackageList>");

			logger.info("退保发送数据：" + buff_xml.toString() + "\n");

			// jdbcTemplate.update(
			// "UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
			// new Object[] { 5, BatchNo });

			List<Map<String, Object>> send_pa = jdbcTemplate.queryForList(
					"SELECT interface, interkey FROM insproject WHERE id  = ?", new Object[] { projectid });

			if (send_pa == null || send_pa.size() == 0) {
				out.print("方案错误");
				return;
			}

			final String interface_url = (String) send_pa.get(0).get("interface");
			final String interkey = (String) send_pa.get(0).get("interkey");

			String result = CommsBx.sendInfo2BxGetWay(jdbcTemplate, interkey, buff_xml, interface_url, BatchNo);

			logger.info("退保返回xml：" + result);
			if (result == null || result.indexOf("err") != -1) {

				out.print("网关服务器异常");

				return;
			}

			if (result.indexOf("<FailReason>") != -1) {
				Pattern p = Pattern.compile("<FailReason>(.*)</FailReason>");
				Matcher m = p.matcher(result);
				while (m.find()) {
					out.print("订单号为：" + BatchNo + "退保失败! 原因： " + m.group(1));
					return;
				}
			}

			if (result.indexOf("<IsSuccess>1</IsSuccess>") == -1) {

				out.print("退保失败");
			}

			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[] { 9, BatchNo });

			// jdbcTemplate
			// .update("INSERT INTO orderlog (batchno,front,behind,note) VALUES
			// (?,7,9,'退保成功')",
			// new Object[] { BatchNo });

			logger.info("订单号: " + BatchNo + " 退保成功");
			out.print("0000");
			return;
		} else if (companyid == 7) {

			List<Map<String, Object>> res = jdbcTemplate.queryForList(
					"SELECT GrpContNo FROM picc_teamorder WHERE batchno  = ? and status <> 9",
					new Object[] { BatchNo });

			if (res == null || res.size() == 0) {
				out.print("订单号：" + BatchNo + " 不存在，或已退保");
				return;
			}

			final StringBuffer buff_xml = new StringBuffer();
			buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buff_xml.append("<TransData>");
			buff_xml.append("<InputData>");
			buff_xml.append("<ContNo>" + res.get(0).get("GrpContNo") + "</ContNo>");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String Time = sdf.format(new Date());
			buff_xml.append("<EdorDate>" + Time + "</EdorDate>");
			buff_xml.append("<AppntEmail>N</AppntEmail><EdorReason>N</EdorReason>");
			buff_xml.append("</InputData></TransData>");
			EServerManageService ems = new EServerManageService();
			EServerManage em = ems.getPort(EServerManage.class);

			System.out.println(buff_xml.toString());

			String s = em.cancelPolicy(buff_xml.toString(), "46");

			System.out.println(s);

			if (s.indexOf("<Flag>-1</Flag>") != -1) {
				Pattern p = Pattern.compile("<Result>(.*)</Result>");
				Matcher m = p.matcher(s);
				while (m.find()) {
					out.print("退保失败：" + m.group(1));
					jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
							new Object[] { 7, m.group(1), BatchNo });
					return;
				}

			}
			logger.info("订单号: " + BatchNo + " 退保成功");

			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?,backtime = now() WHERE batchno = ?",
					new Object[] { 9, BatchNo });

			out.print("0000");
		} else if (companyid == 8) {

			List<Map<String, Object>> res = jdbcTemplate.queryForList(
					"SELECT GrpContNo FROM picc_teamorder WHERE batchno  = ? and status <> 9",
					new Object[] { BatchNo });

			if (res == null || res.size() == 0) {
				out.print("订单号：" + BatchNo + " 不存在，或已退保");
				return;
			}

			final StringBuffer buff_xml = new StringBuffer();
			buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buff_xml.append("<request><header>");
			buff_xml.append("<msgId>srv0002</msgId>");

			Date d = new Date();
			SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssS");
			String timestamp = s.format(d);

			buff_xml.append("<timestamp>" + timestamp + "</timestamp>");
			buff_xml.append("<version>1.0</version>");
			buff_xml.append("<ipaddress>139.196.105.157</ipaddress>");
			buff_xml.append("</header>");
			buff_xml.append("<body>");
			buff_xml.append("<policyno>" + res.get(0).get("GrpContNo") + "</policyno>");
			buff_xml.append("<operatecode>1234567890</operatecode>");
			buff_xml.append("</body>");
			buff_xml.append("</request>");

			CommServerWebService ems = new CommServerWebService();
			CommServerWebServicePortType em = ems.getPort(CommServerWebServicePortType.class);

			System.out.println("都邦退保请求：" + buff_xml.toString());

			String es = MD5.md5("instony1234" + buff_xml.toString());
			String s1 = em.generatePolicyService(buff_xml.toString(), es);

			System.out.println("都邦退保返回：" + s1);

			if (s1.indexOf("<resultcode>0</resultcode>") == -1) {

				Pattern p = Pattern.compile("<errordesc>(.*)</errordesc>");
				Matcher m = p.matcher(s1);

				//
				while (m.find()) {
					out.print("都邦退保失败：" + m.group(1));

					return;
				}
			}

			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?,backtime = now() WHERE batchno = ?",
					new Object[] { 9, BatchNo });
			out.println("0000");

		}else if (companyid == 9){
			List<Map<String, Object>> res = jdbcTemplate.queryForList(
					"SELECT GrpContNo FROM picc_teamorder WHERE batchno  = ? and status <> 9",
					new Object[] { BatchNo });

			if (res == null || res.size() == 0) {
				out.print("订单号：" + BatchNo + " 不存在，或已退保");
				return;
			}
			
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpPost httppost = new HttpPost("http://e-policy.apiins.com/PolicyCancelServlet.do");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			formparams.add(new BasicNameValuePair("policyNo", (String)res.get(0).get("GrpContNo")));
			formparams.add(new BasicNameValuePair("userCode", "MA001067"));
			formparams.add(new BasicNameValuePair("password", "7C3B871A578D6EB4C1F591B0139B63CD"));
			UrlEncodedFormEntity uefEntity;
			try {
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				httppost.setEntity(uefEntity);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			HttpResponse response1;
			try {
				response1 = httpclient.execute(httppost);
				HttpEntity resEntity = response1.getEntity();
				InputStreamReader reader2 = new InputStreamReader(resEntity.getContent(), "UTF-8");
				char[] buff = new char[1024];
				int length = 0;
				String t = "";
				while ((length = reader2.read(buff)) != -1) {
					t += new String(buff, 0, length);
				}
				System.out.println(t + "=========");
			}catch (ClientProtocolException e) {
				out.print("err:发送保险网关平台失败1001");
				return ;
			} catch (IOException e) {
				out.print("err:发送保险网关平台失败1002");
				return ;
			}
			
		}else if(companyid == 10){
			out.println(rmjTb(BatchNo, jdbcTemplate, Integer.parseInt(mid)));
		}else if(companyid == 11){
			out.println(rmcTb(BatchNo, jdbcTemplate));
		}
		else {
			out.print("companyid err");
			return;
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnsupportedEncodingException {
		ApplicationContext context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
		final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
		System.out.println(rmcTb("BYW2017062815271167269", jdbcTemplate));
	}

	
	
	public static String rmjTb(String BatchNo,JdbcTemplate jdbcTemplate,int mid) throws UnsupportedEncodingException {
	
		List<Map<String, Object>> teamorderList = jdbcTemplate
				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });
		Date d = new Date();
		SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat r = new SimpleDateFormat("HH:mm:ss");
		String r1 = r.format(d);
		String t1 = t.format(d);
		
		final StringBuffer buff_xml = new StringBuffer();
		buff_xml.append("<fd:GRPDELETEDATA xmlns:fd=\"http://example.org/filedata\">");
		buff_xml.append("<MSGHEAD>");
		buff_xml.append("<ITEM>");
		buff_xml.append("<BatchNo>"+BatchNo+"</BatchNo>");
		buff_xml.append("<SendDate>"+t1+"</SendDate>");
		buff_xml.append("<SendTime>"+r1+"</SendTime>");
//		buff_xml.append("<BranchCode>EC_WX</BranchCode>");
		buff_xml.append("<SendOperator>GEJB</SendOperator>");
		buff_xml.append("<MsgType>WX_GRP03</MsgType>");
		buff_xml.append("</ITEM>");
		buff_xml.append("</MSGHEAD>");
		buff_xml.append("<GRPDELETELIST><ITEM>");
		buff_xml.append("<BranchCode>"+teamorderList.get(0).get("grpname")+"</BranchCode>");
		buff_xml.append("<GrpContNo>"+teamorderList.get(0).get("GrpContNo")+"</GrpContNo>");
		buff_xml.append("<PrtNo>"+teamorderList.get(0).get("PrtNo")+"</PrtNo>");
		Map<String, Object> m10 = jdbcTemplate
				.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND  insid = 10 AND kind = 4",
				new Object[] { mid });
		buff_xml.append("<ManageCom>" + m10.get("agentcode") + "</ManageCom>");
		buff_xml.append("<ReMark>123</ReMark>");
		if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 10 AND kind=1") == 0) {
			return "AgentCom error";
		}
		if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 10 AND kind=2") == 0) {
			return "AgentCode error";
		}
		
		buff_xml.append("<Token>999999</Token>");
		
//		Map<String, Object> m = jdbcTemplate
//				.queryForMap("SELECT agentcode FROM toinscompany WHERE insid = 10 AND kind=1");
//
//		Map<String, Object> m1 = jdbcTemplate
//				.queryForMap("SELECT agentcode FROM toinscompany WHERE insid=10 AND kind=2");
//
//		buff_xml.append("<AgentCom>" + m.get("agentcode") + "</AgentCom>");
//		buff_xml.append("<AgentCode>" + m1.get("agentcode") + "</AgentCode>");
//		
//		buff_xml.append(
//				"<CValiDate>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10) + "</CValiDate>");
//		buff_xml.append("<CValiTime></CValiTime>");
//		buff_xml.append("<CInValiDate>" + teamorderList.get(0).get("CInValiDate").toString().substring(0, 10)
//				+ "</CInValiDate>");
//		buff_xml.append("<CInValiTime></CInValiTime>");
//		buff_xml.append("<PayIntv></PayIntv>");
//		buff_xml.append("<PayMode></PayMode>");
//		buff_xml.append("<PolApplyDate></PolApplyDate>");
		buff_xml.append("</ITEM></GRPDELETELIST></fd:GRPDELETEDATA>");
		
		System.out.println(buff_xml);
		
		
		String encode = buff_xml + "";
		String s2 = "sendXml=" + URLEncoder.encode(URLEncoder.encode(encode, "UTF-8"), "UTF-8");

		String res = S1.sendPost("http://124.127.37.56:9088/hk", s2);
		
		if("9999".equals(res)){
			return  "发送 POST 请求出现异常";
		}
		

		String res_1 = URLDecoder.decode(res, "UTF-8");
		
		System.out.println("人保健退保返回:==================>>>>"+res_1);

		if (res_1.indexOf("<State>00</State>") >= 0) {
			
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?,backtime = now() WHERE batchno = ?",
					new Object[] { 9, BatchNo });
			return "0000";
		}else{
			Pattern p = Pattern.compile("<ErrInfo>(.*)</ErrInfo>");
			Matcher mr = p.matcher(res_1);
			String errorInfo = "";
			while (mr.find()) {
				errorInfo = mr.group(1);
//				jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?,backtime = now() WHERE batchno = ?",
//						new Object[] { 9, BatchNo });
			}
			return errorInfo;
		}
	}
	
	public static String rmcTb(String BatchNo,JdbcTemplate jdbcTemplate) {
		
		/**
		 * <?xml version="1.0" encoding="GB2312" standalone="yes"?>
<PolicyEndorsement>
	<Head>			
		<UUID>公司名称缩写（拼音或英文）+yyyyMMddhhmmssyyy </UUID>   //报文唯一性约束
		<PlateformCode>平台项目标识</PlateformCode>    
		<Md5Value> MD5加密校验</Md5Value>
	</Head>
	<EndorseInfos>
<EndorseInfo>
			<PolicyNo>保单号</PolicyNo>
			<EndorseType>批改类型(00)</EndorseType>
			<EndorseDate>2013-02-01 00:00:00</EndorseDate>批改时间及格式
		</EndorseInfo>
	</EndorseInfos>
</PolicyEndorsement>

		 */
		

//		List<Map<String, Object>> teamorderList = jdbcTemplate
//				.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });
		Date d = new Date();
		SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String t1 = t.format(d);
		
		List<Map<String, Object>> r1 = jdbcTemplate
				.queryForList("SELECT GrpContNo  FROM picc_teamorder WHERE BatchNo = ?", new Object[] { BatchNo });
		
		
		final StringBuffer buff_xml = new StringBuffer();
		buff_xml.append("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?>");
		buff_xml.append("<PolicyEndorsement>");
		buff_xml.append("<Head>");
		String uuid = "BYW" + System.currentTimeMillis();
		buff_xml.append("<UUID>" + uuid + "</UUID>");
		Map<String, Object> m = jdbcTemplate
				.queryForMap("SELECT agentcode FROM toinscompany WHERE insid = 11 AND kind=1");
		buff_xml.append("<PlateformCode>" + m.get("agentcode") + "</PlateformCode>");
		buff_xml.append("<Md5Value>"
				+ MD5.md5(uuid+r1.get(0).get("GrpContNo"))
				+ "</Md5Value>");
		buff_xml.append("</Head>");
		buff_xml.append("<EndorseInfos>");
		buff_xml.append("<EndorseInfo>");
		buff_xml.append("<PolicyNo>" + r1.get(0).get("GrpContNo") + "</PolicyNo>");
		buff_xml.append("<EndorseType>00</EndorseType>");
		buff_xml.append("<EndorseDate>"+t1+"</EndorseDate>");
		buff_xml.append("</EndorseInfo>");
		buff_xml.append("</EndorseInfos>");
		buff_xml.append("</PolicyEndorsement>");
		
		
		
		EcooperationWebServiceService ewss = new EcooperationWebServiceService();
		EcooperationWebService ews = ewss.getPort(EcooperationWebService.class);
		String s = ews.modifyService("001003", buff_xml.toString());
		
		System.out.println(s);
		if(s.indexOf("<ErrorCode>00</ErrorCode>")>0){
			jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?,backtime = now() WHERE batchno = ?",
					new Object[] { 9, BatchNo });
			return "0000";
		}else{
			
			return buff_xml.toString();
		}
	}
}
