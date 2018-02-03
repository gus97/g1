package com.gopay;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

import com.gus.AESCoder;

/**
 * 
 * @author gus
 * 
 */
public class GoPayDemo extends HttpServlet {

	private static final long serialVersionUID = -5860142703343915134L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=" + "UTF-8");
		//response.setCharacterEncoding("UTF-8");
		
		// 事先定义好的密钥，传输以下数据用AES加密
		String key = "3b36fd660116ff223b5370cf909fe44d";
		Map<String, String> map = new TreeMap<String, String>();
		String bankcode = request.getParameter("bankcode");
		List<String> bank = new ArrayList<String>();
		bank.add("CCB");
		bank.add("CMB");
		bank.add("ICBC");
		bank.add("BOC");
		bank.add("ABC");
		bank.add("BOCOM");
		bank.add("CEB");
		
		if(!bank.contains(bankcode)){
			response.getWriter().write("测试参数：bank=(CCB,CMB,ICBC,BOC,ABC,BOCOM,CEB)");
			return;
		}
		
		try {
			// 订单号，确保当前商户唯一
			map.put("merOrderNum",
					AESCoder.encrypt(System.currentTimeMillis()+"", key));
			// 金额 单位： 元
			map.put("tranAmt", AESCoder.encrypt("783600000", key));
			// IP
			map.put("tranIP", request.getRemoteAddr());
			// 假设CEB 光大
			map.put("bankCode", AESCoder.encrypt(bankcode, key));
			// 密钥 从数据库
			map.put("VerficationCode", AESCoder.encrypt("helloBx909", key));
			// 入账号从数据库
			map.put("virCardNoIn", AESCoder.encrypt("0000000002000002145", key));
			// 商户号 从数据库
			map.put("merchantID", AESCoder.encrypt("0000005765", key));

			String actionUrl = "http://121.40.52.64/gopay/Send2GoPay";

			StringBuilder html = new StringBuilder();
			html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
			html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"")
					.append(actionUrl).append("\" method=\"post\">\n");

			for (String m_key : map.keySet()) {
				html.append("<input type=\"hidden\" name=\"" + m_key
						+ "\" id=\"" + m_key + "\" value=\"" + map.get(m_key)
						+ "\">\n");
			}
			html.append("</form>\n");

			
			response.getWriter().write(html.toString());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}

	}
}
