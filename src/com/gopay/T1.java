package com.gopay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class T1 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1643106523798136499L;

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/xml"); 
		//response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter(); 
		
		//System.out.println(request.getParameter("sign"));
		String x = "";
          InputStreamReader reader = new InputStreamReader(request.getInputStream(), "UTF-8"); 
          char[] buff = new char[1024]; 
          int length = 0; 
          while ((length = reader.read(buff)) != -1) { 
                  x += new String(buff, 0, length); 
                 
          } 
          
          System.out.println(x);
          
          File f = new File("d:/2.xml");
  		
  		FileInputStream fip = new FileInputStream(f);
  		
  		InputStreamReader reader2 = new InputStreamReader(fip, "UTF-8");
   
  		StringBuffer sb = new StringBuffer();
  		while (reader2.ready()) {
  			sb.append((char) reader2.read());
  		}
  		reader2.close();
  		fip.close();
         
        out.print(sb.toString());
        
	}
}
