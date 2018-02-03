package com.gopay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Mapp {
	
	private static Logger logger = Logger.getLogger("op");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws HttpException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			DocumentException {

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"com/gopay/spring_c3p0.xml");
		JdbcTemplate jdbcTemplate = (JdbcTemplate) context
				.getBean("jdbcTemplate");

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//String Time = sdf.format(new Date());
		
		List<Map<String, Object>> res = jdbcTemplate
				.queryForList("SELECT * FROM query_gopay WHERE STATUS = 0 AND times >  DATE_SUB(NOW(),INTERVAL 2 DAY) ");

		int cnt_bill = 0;
		for (Map map : res) {
			String bno = (String) map.get("batchno");
			String merid = (String) map.get("merid");
			String otime = (String) map.get("otime");
			String key = (String) map.get("key_se");

			String url = "https://gateway.gopay.com.cn/Trans/WebClientAction.do";

			NameValuePair tranCode = new NameValuePair("tranCode", "4020");
			NameValuePair tranDateTime = new NameValuePair("tranDateTime",
					otime);
			NameValuePair merOrderNum = new NameValuePair("merOrderNum", bno);
			NameValuePair merchantID = new NameValuePair("merchantID", merid);
			NameValuePair orgOrderNum = new NameValuePair("orgOrderNum", bno);
			NameValuePair orgtranDateTime = new NameValuePair(
					"orgtranDateTime", otime);
			NameValuePair orgTxnType = new NameValuePair("orgTxnType", "");
			NameValuePair orgtranAmt = new NameValuePair("orgtranAmt", "");
			NameValuePair orgTxnStat = new NameValuePair("orgTxnStat", "");
			NameValuePair authID = new NameValuePair("authID", "");
			NameValuePair isLocked = new NameValuePair("isLocked", "");
			NameValuePair respCode = new NameValuePair("respCode", "");
			NameValuePair tranIP = new NameValuePair("tranIP", "127.0.0.1");
			NameValuePair msgExt = new NameValuePair("msgExt", "127.0.0.1");
			NameValuePair merchantEncode = new NameValuePair("merchantEncode",
					"");

			String plain = "tranCode=[" + tranCode.getValue() + "]merchantID=["
					+ merchantID.getValue() + "]merOrderNum=["
					+ merOrderNum.getValue()
					+ "]tranAmt=[]ticketAmt=[]tranDateTime=["
					+ tranDateTime.getValue()
					+ "]currencyType=[]merURL=[]customerEMail=[]authID=["
					+ authID.getValue() + "]orgOrderNum=["
					+ orgOrderNum.getValue() + "]orgtranDateTime=["
					+ orgtranDateTime.getValue() + "]orgtranAmt=["
					+ orgtranAmt.getValue() + "]orgTxnType=["
					+ orgTxnType.getValue() + "]orgTxnStat=["
					+ orgTxnStat.getValue() + "]msgExt=[" + msgExt.getValue()
					+ "]virCardNo=[]virCardNoIn=[]tranIP=[" + tranIP.getValue()
					+ "]isLocked=[" + isLocked.getValue()
					+ "]feeAmt=[]respCode=[" + respCode.getValue()
					+ "]VerficationCode=[" + key + "]";

			String signValue_A = sign(plain);

			NameValuePair signValue = new NameValuePair("signValue",
					signValue_A);

			NameValuePair[] data = { tranCode, tranDateTime, merOrderNum,
					merchantID, orgOrderNum, orgtranDateTime, orgTxnType,
					orgtranAmt, orgTxnStat, authID, isLocked, respCode, tranIP,
					msgExt, merchantEncode, signValue };

			String response = methodPost(url, data);

			// if (response.indexOf("errCode") == -1) {

			//System.out.println(bno + "\n" + response);

			Map<String, String> maps;

			maps = readStringXmlOut(response);

			if (maps != null) {
				String status = (String) maps.get("orgTxnStat");
				logger.info("bill check: ");
				if (status.equals("20000")) {
					cnt_bill ++;
					logger.info(status+"--"+maps.get("orgOrderNum"));
					jdbcTemplate
							.update("UPDATE picc_teamorder SET STATUS = 4 WHERE batchno = ?",
									new Object[] { maps.get("orgOrderNum") });

					jdbcTemplate
							.update("UPDATE query_gopay SET STATUS = 1 WHERE batchno = ?",
									maps.get("orgOrderNum"));

				}
			}

		}
		
		logger.info("bill check finished cnt = "+cnt_bill);

	}

	public static String methodPost(String url, NameValuePair[] data) {

		String response = "";// 要返回的response信息
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		int statusCode = 0;
		try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
		// 301或者302
		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
				|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			// 从头中取出转向的地址
			Header locationHeader = postMethod.getResponseHeader("location");
			String location = null;
			if (locationHeader != null) {
				location = locationHeader.getValue();
				System.out.println("The page was redirected to:" + location);
				response = methodPost(location, data);// 用跳转后的页面重新请求。
			} else {
				System.err.println("Location field value is null.");
			}
		} else {
			//System.out.println(postMethod.getStatusLine());

			try {
				response = postMethod.getResponseBodyAsString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			postMethod.releaseConnection();
		}
		return response;
	}

	public static final String sign(String src)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final int i4 = 4;
		final int i0xf = 0xf;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		byte[] strTemp = src.getBytes("UTF-8");
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		/* 转换为16进制 */
		int j = md.length;
		char[] str = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> i4 & i0xf];
			str[k++] = hexDigits[byte0 & i0xf];
		}
		return new String(str);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map readStringXmlOut(String xml) {
		Map map = new HashMap();
		try {

			Document doc = null;

			doc = DocumentHelper.parseText(xml); // 将字符串转为XML

			Element rootElt = doc.getRootElement(); // 获取根节点

			//System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

			Iterator iter1 = rootElt.elementIterator("orgTxnStat"); // 获取根节点下的子节点head

			Element itemEle1 = (Element) iter1.next();

			Iterator iter2 = rootElt.elementIterator("orgOrderNum");

			Element itemEle2 = (Element) iter2.next();

			map.put("orgOrderNum", itemEle2.getText());

			map.put("orgTxnStat", itemEle1.getText());

			return map;
		} catch (Exception e) {

		}
		return null;
	}
}
