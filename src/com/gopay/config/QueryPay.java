package com.gopay.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QueryPay extends HttpServlet {

	private static final long serialVersionUID = -8018282409073605133L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		
	}

	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tranCode = request.getParameter("tranCode");
		String tranDateTime = request.getParameter("tranDateTime");
		String merOrderNum = request.getParameter("merOrderNum");
		String merchantID = request.getParameter("merchantID");
		String orgOrderNum = request.getParameter("orgOrderNum");
		String orgtranDateTime = request.getParameter("orgtranDateTime");
		String orgTxnType= request.getParameter("orgTxnType");
		String orgtranAmt = request.getParameter("orgtranAmt");
		String orgTxnStat = request.getParameter("orgTxnStat");
		String authID = request.getParameter("authID");
		String isLocked = request.getParameter("isLocked");
		String respCode = request.getParameter("respCode");
		String tranIP = request.getParameter("tranIP");
		String msgExt = request.getParameter("msgExt");
		String merchantEncode = request.getParameter("merchantEncode");
		String VerficationCode = request.getParameter("VerficationCode");
	}

}
