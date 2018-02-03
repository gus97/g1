package com.gopay;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gopay.config.GopayConfig;
import com.gopay.utils.GopayUtils;
import com.gus.AESCoder;

public class Send2GoPay extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659065402487338370L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("merOrderNum:"
				+ request.getParameter("merOrderNum") + "\n" + "tranAmt:"
				+ request.getParameter("tranAmt") + "\n" + "tranIP:"
				+ request.getParameter("tranIP") + "\n" + "bankCode:"
				+ request.getParameter("bankCode") + "\n" + "VerficationCode:"
				+ request.getParameter("VerficationCode") + "\n"
				+ "virCardNoIn:" + request.getParameter("virCardNoIn"));

		String merOrderNum;
		try {
			merOrderNum = AESCoder.decrypt(request.getParameter("merOrderNum"),
					CommVar.AES_KEY);
			String tranAmt = AESCoder.decrypt(request.getParameter("tranAmt"),
					CommVar.AES_KEY);

			String tranIP = request.getParameter("tranIP");
			String bankCode = AESCoder.decrypt(
					request.getParameter("bankCode"), CommVar.AES_KEY);
			String VerficationCode = AESCoder.decrypt(
					request.getParameter("VerficationCode"), CommVar.AES_KEY);
			String merchantID = AESCoder.decrypt(
					request.getParameter("merchantID"), CommVar.AES_KEY);
			String virCardNoIn = AESCoder.decrypt(
					request.getParameter("virCardNoIn"), CommVar.AES_KEY);

			Map<String, String> map = new TreeMap<String, String>();
			String version = CommVar.version;
			map.put("version", CommVar.version);
			map.put("charset", CommVar.charset);
			map.put("language", CommVar.language);
			map.put("signType", CommVar.signType);
			String tranCode = CommVar.tranCode_pay;
			map.put("tranCode", CommVar.tranCode_pay);

			map.put("merchantID", CommVar.merchantID);

			map.put("merOrderNum", merOrderNum);

			map.put("tranAmt", tranAmt);
			String feeAmt = "0";
			map.put("feeAmt", "0");
			map.put("currencyType", CommVar.currencyType);
			String frontMerUrl = "";
			map.put("frontMerUrl", "");
			String backgroundMerUrl = CommVar.backgroundMerUrl;
			map.put("backgroundMerUrl", backgroundMerUrl);
			String tranDateTime = GopayUtils.getGopayServerTime();
			map.put("tranDateTime", tranDateTime);
			map.put("virCardNoIn", virCardNoIn);

			map.put("tranIP", tranIP);
			map.put("isRepeatSubmit", CommVar.isRepeatSubmit);
			map.put("goodsName", "");
			map.put("goodsDetail", "");
			map.put("buyerName", "");
			map.put("buyerContact", "");
			map.put("merRemark1", "");
			map.put("merRemark2", "");
			map.put("bankCode", bankCode);

			map.put("bankCode", bankCode);
			map.put("userType", CommVar.userType);

			// String VerficationCode = "helloBx909";
			map.put("VerficationCode", VerficationCode);
			String gopayServerTime = tranDateTime;

			ApplicationContext ctx = WebApplicationContextUtils
					.getWebApplicationContext(this.getServletContext());
			JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx
					.getBean("jdbcTemplate");

			jdbcTemplate
					.update("INSERT INTO query_gopay (batchno,otime,merid,key_se,status) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE otime = VALUES(otime)",
							new Object[] { merOrderNum, gopayServerTime,
									merchantID, VerficationCode, 0 });

			// 组织加密明文
			String plain = "version=[" + version + "]tranCode=[" + tranCode
					+ "]merchantID=[" + merchantID + "]merOrderNum=["
					+ merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=["
					+ feeAmt + "]tranDateTime=[" + tranDateTime
					+ "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=["
					+ backgroundMerUrl
					+ "]orderId=[]gopayOutOrderId=[]tranIP=[" + tranIP
					+ "]respCode=[]gopayServerTime=[" + gopayServerTime
					+ "]VerficationCode=[" + VerficationCode + "]";

			String signValue = GopayUtils.md5(plain);

			map.put("signValue", signValue);

			map.put("gopayServerTime", gopayServerTime);

			String actionUrl = GopayConfig.gopay_gateway;

			StringBuilder html = new StringBuilder();
			html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
			html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"")
					.append(actionUrl).append("\" method=\"post\">\n");

			for (String key : map.keySet()) {
				html.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
						+ key + "\" value=\"" + map.get(key) + "\">\n");
			}
			html.append("</form>\n");


			response.setContentType("text/html;charset=" + "UTF-8");
			//response.setCharacterEncoding("UTF-8");
			response.getWriter().write(html.toString());
		} catch (Exception e) {

			response.setContentType("text/html;charset=" + "UTF-8");
			//response.setCharacterEncoding("UTF-8");

			response.getWriter().print("<hr>");
			response.getWriter().print("请检查传送数据的完整性.");
			response.getWriter().print("<hr>");

			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		System.out
				.println("<GopayTranRes><errCode>100E5041</errCode><errMessage>订单不存在</errMessage></GopayTranRes>"
						.indexOf("<errCode>"));
	}
}
