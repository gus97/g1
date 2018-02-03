package com.allinpay;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;

import com.allinpay.ets.client.SecurityUtil;
import com.gopay.CommVar;
import com.gus.AESCoder;

public class Send2AllinPay extends HttpServlet {

	private static final long serialVersionUID = 7848152065444251381L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		response.setContentType("text/html;charset=" + "UTF-8");

		// String se_key = AllinPayCFG.ALL_IN_PAY_KEY;
		// String version = AllinPayCFG.ALL_IN_PAY_VERSION;
		// String language = AllinPayCFG.ALL_IN_PAY_LANGUAGE;
		// String inputCharset = AllinPayCFG.ALL_IN_PAY_CHAR_SET;
		// String merchantId = AllinPayCFG.ALL_IN_PAY_MID;
		// String pickupUrl = AllinPayCFG.ALL_IN_PAY_P_URL;
		// String receiveUrl = AllinPayCFG.ALL_N_PAY_RES_URL;
		// String signType = AllinPayCFG.ALL_IN_PAY_SIN_TYPE;

		String se_key = "1234567890";
		String version = "v1.0";
		String language = "1";
		String inputCharset = "1";
		String merchantId = "100020091218001";
		String pickupUrl = "http://121.40.52.64/";
		String receiveUrl = "http://121.40.52.64/gopay/AIP_RES";
		String signType = "1";

		// ===================================================
		//String payType = request.getParameter("ptype");
		
		//默认0
		String payType = "0";
		//String issuerId = request.getParameter("issuid");
		
		//默认为空
		String issuerId = "";
		
		try {
			//batchno
			String orderNo = AESCoder.decrypt(request.getParameter("oid"),
					CommVar.AES_KEY);
			
			//单位分
			String orderAmount = AESCoder.decrypt(request.getParameter("fee"),
					CommVar.AES_KEY);
			// ===================================================

			Date d = new Date();
			SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");

			String orderDatetime = s.format(d);

			String orderCurrency = "0";
			String orderExpireDatetime = "60";
			String payerTelephone = "18652003900";
			String payerEmail = "390154333@qq.com";
			String payerName = "gus97";
			String payerIDCard = "";
			String pid = "";
			String productName = "测试商品";
			String productId = "bx";
			String productNum = "1";
			String productPrice = "0";
			String productDesc = "bx";
			String ext1 = "123";
			String ext2 = "123";
			String extTL = "";// 通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务

			String pan = "";
			String tradeNature = "GOODS";
			// String sign = "";

			// 若直连telpshx渠道，payerTelephone、payerName、payerIDCard、pan四个字段不可为空
			// 其中payerIDCard、pan需使用公钥加密（PKCS1格式）后进行Base64编码
			if (null != payerIDCard && !"".equals(payerIDCard)) {
				try {
					// payerIDCard =
					// SecurityUtil.encryptByPublicKey("C:\\TLCert.cer",
					// payerIDCard);
					payerIDCard = SecurityUtil.encryptByPublicKey(
							"d:/TLCert-test.cer", payerIDCard);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != pan && !"".equals(pan)) {
				try {
					pan = SecurityUtil.encryptByPublicKey("d:/TLCert-test.cer",
							pan);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 构造订单请求对象，生成signMsg。
			com.allinpay.ets.client.RequestOrder requestOrder = new com.allinpay.ets.client.RequestOrder();
			if (null != inputCharset && !"".equals(inputCharset)) {
				requestOrder.setInputCharset(Integer.parseInt(inputCharset));
			}
			requestOrder.setPickupUrl(pickupUrl);
			requestOrder.setReceiveUrl(receiveUrl);
			requestOrder.setVersion(version);
			if (null != language && !"".equals(language)) {
				requestOrder.setLanguage(Integer.parseInt(language));
			}
			requestOrder.setSignType(Integer.parseInt(signType));
			requestOrder.setPayType(Integer.parseInt(payType));
			requestOrder.setIssuerId(issuerId);
			requestOrder.setMerchantId(merchantId);
			requestOrder.setPayerName(payerName);
			requestOrder.setPayerEmail(payerEmail);
			requestOrder.setPayerTelephone(payerTelephone);
			requestOrder.setPayerIDCard(payerIDCard);
			requestOrder.setPid(pid);
			requestOrder.setOrderNo(orderNo);
			requestOrder.setOrderAmount(Long.parseLong(orderAmount));
			requestOrder.setOrderCurrency(orderCurrency);
			requestOrder.setOrderDatetime(orderDatetime);
			requestOrder.setOrderExpireDatetime(orderExpireDatetime);
			requestOrder.setProductName(productName);
			if (null != productPrice && !"".equals(productPrice)) {
				requestOrder.setProductPrice(Long.parseLong(productPrice));
			}
			if (null != productNum && !"".equals(productNum)) {
				requestOrder.setProductNum(Integer.parseInt(productNum));
			}
			requestOrder.setProductId(productId);
			requestOrder.setProductDesc(productDesc);
			requestOrder.setExt1(ext1);
			requestOrder.setExt2(ext2);
			requestOrder.setExtTL(extTL);// 通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
			requestOrder.setPan(pan);
			requestOrder.setTradeNature(tradeNature);
			requestOrder.setKey(se_key); // key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。

			// String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
			String signMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。

			Map<String, String> map = new TreeMap<String, String>();

			map.put("inputCharset", inputCharset);
			map.put("pickupUrl", pickupUrl);
			map.put("receiveUrl", receiveUrl);
			map.put("version", version);
			map.put("language", language);
			map.put("signType", signType);
			map.put("merchantId", merchantId);
			map.put("payerName", payerName);
			map.put("payerEmail", payerEmail);
			map.put("payerTelephone", payerTelephone);
			map.put("payerIDCard", payerIDCard);
			map.put("pid", pid);
			map.put("orderNo", orderNo);
			map.put("orderAmount", orderAmount);
			map.put("orderCurrency", orderCurrency);
			map.put("orderDatetime", orderDatetime);
			map.put("orderExpireDatetime", orderExpireDatetime);
			map.put("productName", productName);
			map.put("productPrice", productPrice);
			map.put("productNum", productNum);
			map.put("productId", productId);
			map.put("productDesc", productDesc);
			map.put("ext1", ext1);
			map.put("ext2", ext2);
			map.put("payType", payType);
			map.put("pan", pan);
			map.put("tradeNature", tradeNature);
			map.put("signMsg", signMsg);

			// System.out.println("=========" + strSrcMsg + "==========");

			// ApplicationContext ctx = WebApplicationContextUtils
			// .getWebApplicationContext(this.getServletContext());
			// JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx
			// .getBean("jdbcTemplate");
			//
			// jdbcTemplate
			// .update("INSERT INTO query_gopay (batchno,otime,merid,key_se,status) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE otime = VALUES(otime)",
			// new Object[] { orderNo, gopayServerTime,
			// merchantID, VerficationCode, 0 });

			String actionUrl = AllinPayCFG.ALL_IN_PAY_URL;

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

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, DecoderException {

		
		
		System.out.println(AESCoder.encrypt("500", CommVar.AES_KEY));
		System.out.println(AESCoder.encrypt("no1111111123", CommVar.AES_KEY));
	}

}
