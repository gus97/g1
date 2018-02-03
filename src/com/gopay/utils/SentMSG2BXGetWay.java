package com.gopay.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class SentMSG2BXGetWay extends HttpServlet {
	private static final long serialVersionUID = -7217292821817721091L;
	private static Logger logger = Logger.getLogger("op");

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Pragma", "No-Cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-Cache");
		request.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setContentType("text/html;charset=" + "UTF-8");
		//response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String merOrderNum = request.getParameter("bno");
		
		String pg = request.getParameter("pg");
		
		String subno = request.getParameter("subno");
		
		if (pg != null && !"".equals(pg)) {
			
			out.print(SendBxOrder.pgbw(merOrderNum, pg,subno));
			
		}else{
		
		String pid = request.getParameter("projectid");
		if (pid == null || !isNumeric(pid)) {
			out.print("projectid err");
			return;
		}

		String cid = request.getParameter("companyid");
		
		System.out.println("companyid:"+cid);
		if (cid == null || !isNumeric(cid)) {
			out.print("companyid err");
			return;
		}
		
		

		String mid = request.getParameter("medid");
		if (mid == null || !isNumeric(mid)) {
			out.print("medid err");
			return;
		}

		String res = null;
		String sync = request.getParameter("sync");
		if (sync != null && "1".equals(sync)) {
			res = SendBxOrder.sendOrder2GetwaySync(merOrderNum,
					new Integer(pid), new Integer(cid), new Integer(mid));
		} else {
			res = SendBxOrder.sendOrder2Getway(merOrderNum, new Integer(pid),
					new Integer(cid), new Integer(mid));
		}

		logger.info(merOrderNum + " [bx return info] :" + res);
		out.print(res);
		}

	}

	
	//http://121.40.52.64/gopay/SentMSG2BXGetWay?bno=BYW2016071308352899505&projectid=34&companyid=8&medid=1&sync=1
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}
