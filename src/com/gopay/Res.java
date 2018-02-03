package com.gopay;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gopay.utils.GopayUtils;
import com.gopay.utils.SendBxOrder;

public class Res extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("op");

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String version = request.getParameter("version");
		// String charset = request.getParameter("charset");
		// String language = request.getParameter("language");
		// String signType = request.getParameter("signType");
		String tranCode = request.getParameter("tranCode");
		String merchantID = request.getParameter("merchantID");
		String merOrderNum = request.getParameter("merOrderNum");
		String tranAmt = request.getParameter("tranAmt");
		String feeAmt = request.getParameter("feeAmt");
		String frontMerUrl = request.getParameter("frontMerUrl");
		String backgroundMerUrl = request.getParameter("backgroundMerUrl");
		String tranDateTime = request.getParameter("tranDateTime");
		String tranIP = request.getParameter("tranIP");
		String respCode = request.getParameter("respCode");
		// String msgExt = request.getParameter("msgExt");
		String orderId = request.getParameter("orderId");
		String gopayOutOrderId = request.getParameter("gopayOutOrderId");
		// String bankCode = request.getParameter("bankCode");
		// String tranFinishTime = request.getParameter("tranFinishTime");
		// String merRemark1 = request.getParameter("merRemark1");
		// String merRemark2 = request.getParameter("merRemark2");
		String VerficationCode = ResourceBundle.getBundle("mpi").getString(
				"VerficationCode");
		String signValueFromGopay = request.getParameter("signValue");

		// 组织加密明文
		String plain = "version=[" + version + "]tranCode=[" + tranCode
				+ "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum
				+ "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt
				+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=["
				+ frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl
				+ "]orderId=[" + orderId + "]gopayOutOrderId=["
				+ gopayOutOrderId + "]tranIP=[" + tranIP + "]respCode=["
				+ respCode + "]gopayServerTime=[]VerficationCode=["
				+ VerficationCode + "]";

		String signValue = GopayUtils.md5(plain);

		if (signValue.equals(signValueFromGopay)) {

			System.out.println("gopay网关回调ok!" + merOrderNum);

			ApplicationContext ctx = WebApplicationContextUtils
					.getWebApplicationContext(this.getServletContext());
			JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx
					.getBean("jdbcTemplate");

			jdbcTemplate.update(
					"UPDATE picc_teamorder SET STATUS = 4 WHERE batchno = ?",
					new Object[] { merOrderNum });

			int cnt = jdbcTemplate.queryForInt(
					"SELECT COUNT(1) FROM orderlog WHERE batchno = ?",
					new Object[] { merOrderNum });

			if (cnt == 0) {
				jdbcTemplate
						.update("INSERT INTO orderlog (batchno,front,behind,note,dealtime) VALUES (?,10,4,'保险方案订单支付成功，订单变为待提交状态',now())",
								new Object[] { merOrderNum });
			} else {
				jdbcTemplate
						.update("update orderlog set front = 10,behind = 4,dealtime = now(), note = '保险方案订单支付成功，订单变为待提交状态' where batchno = ? ",
								new Object[] { merOrderNum });
			}

			jdbcTemplate
					.update("UPDATE paylog SET state = 2 , realfee = ? , paytime = now() WHERE ordercode = ?",
							new Object[] { new Float(tranAmt), merOrderNum });

			jdbcTemplate.update(
					"UPDATE query_gopay SET STATUS = 1 WHERE batchno = ?",
					new Object[] { merOrderNum });

			// 网银重复调用，防止多次发送请求保单
			int cnt_bx = jdbcTemplate.queryForInt(
					"select * from z1_bx where merOrderNum = ? ",
					new Object[] { merOrderNum });
			if (cnt_bx == 0) {
				logger.info("[bank retuen merOrderNum] = " + merOrderNum);
				jdbcTemplate.update("insert into z1_bx seelct ? ",
						new Object[] { merOrderNum });
				List<Map<String, Object>> ord = jdbcTemplate
						.queryForList(
								"SELECT t.projectid, t.companyid, t.medid FROM picc_teamorder t WHERE t.BatchNo = ? ",
								new Object[] { merOrderNum });
				if (ord != null && ord.size() != 0) {
					String res = SendBxOrder.sendOrder2Getway(merOrderNum,
							(Integer) ord.get(0).get("projectid"),
							(Integer) ord.get(0).get("companyid"),
							(Integer) ord.get(0).get("medid"));
					logger.info(merOrderNum + " [bx return info] :" + res);
				}
			}
			response.getWriter().write(
					"RespCode=0000|JumpURL=http://121.40.52.64/xx/true.jsp");
		}

	}
}
