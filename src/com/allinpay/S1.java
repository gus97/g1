package com.allinpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class S1 {
	
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            
	            System.out.println(urlNameString);
	            
	            
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	           
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream(),"GBK"));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        System.out.println("result:"+result.toString());
	        return result;
	    }

	    /**
	     * 向指定 URL 发送POST方法的请求
	     * 
	     * @param url
	     *            发送请求的 URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return 所代表远程资源的响应结果
	     */
	    public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	           
	            return "9999";
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    
	    
	    public static void main(String[] args) {
	    	//String s1 = "sendXml=<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>GEJBWX_GRP01201418033520026</BatchNo><SendDate>2017-3-1</SendDate><SendTime>15:52:31</SendTime><SendOperator>GEJB</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-3-1</HandlerDate><AgentCom></AgentCom><AgentCode>3202000028</AgentCode><FirstTrialOperator></FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-3-1</ReceiveDate><CoInsuranceFlag>0</CoInsuranceFlag><PayMode>4</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-3-1</CValiDate><CInValiDate></CInValiDate><PeoplesInsu>3</PeoplesInsu><Remark>BBB</Remark></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>郑州晚报</GrpName><Phone>0371-25668532</Phone><OrgancomCode>0205</OrgancomCode><GrpAddress></GrpAddress><GrpZipCode>121275</GrpZipCode><TaxNo>02031</TaxNo><LegalPersonName>张琳</LegalPersonName><LegalPersonIDNo>10210014</LegalPersonIDNo><LinkMan1>张琳</LinkMan1><Phone1>28510003</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType>009</BusinessType><BusinessBigType>新闻</BusinessBigType><GrpNature>09</GrpNature><Peoples>3</Peoples><AppntOnWorkPeoples>3</AppntOnWorkPeoples><AppntOffWorkPeoples>0</AppntOffWorkPeoples><AppntOtherPeoples>0</AppntOtherPeoples><Peoples3>3</Peoples3><OnWorkPeoples>3</OnWorkPeoples><OffWorkPeoples>0</OffWorkPeoples><OtherPeoples>0</OtherPeoples><RelaPeoples>0</RelaPeoples><RelaMatePeoples>0</RelaMatePeoples><RelaYoungPeoples>0</RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>1</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>00</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>谢娜44</InsuredName><Sex>1</Sex><Birthday>1984-3-27</Birthday><IDType>4</IDType><IDNo>19840327</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>度海涛44</InsuredName><Sex>0</Sex><Birthday>1983-03-25</Birthday><IDType>4</IDType><IDNo>19830305</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>xx44</MainInsuredName><InsuredName>庾澄庆44</InsuredName><Sex>0</Sex><Birthday>1970-5-21</Birthday><IDType>4</IDType><IDNo>123836ht0</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys></Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>Y</InsuYearFlag><InsuYear>1</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>800000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>51</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>80000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>28.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1601</RiskCode><DutyCode>601001</DutyCode><FacValAmnt>200</FacValAmnt><FacValAmntType>1</FacValAmntType><Prem>20.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>500000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>3</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685002</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685003</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685004</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>6</Prem></ITEM></WRAPPARAMLIST></GRPCONTDATA>";
	//		String s2 = "sendXml=<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>BYW2017050417512144626</BatchNo><SendDate>2017-05-05</SendDate><SendTime>09:42:46</SendTime><SendOperator>HKXS</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-05-05</HandlerDate><AgentCom>PY002003004001</AgentCom><AgentCode>2232131416</AgentCode><FirstTrialOperator>gzh</FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-05-05</ReceiveDate><CoInsuranceFlag>1</CoInsuranceFlag><PayMode>1</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-05-10</CValiDate><CInValiDate>2018-05-09</CInValiDate><PeoplesInsu>5</PeoplesInsu><Remark></Remark><EjbAgent>86320000</EjbAgent></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>测试22</GrpName><Phone>888888</Phone><OrgancomCode>TEST-11</OrgancomCode><UnifiedSocialCreditNo></UnifiedSocialCreditNo><GrpAddress>cc2</GrpAddress><GrpZipCode></GrpZipCode><TaxNo></TaxNo><LegalPersonName>cc1</LegalPersonName><LegalPersonIDNo>112345678</LegalPersonIDNo><LinkMan1>cc1</LinkMan1><Phone1>888888</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType></BusinessType><BusinessBigType></BusinessBigType><GrpNature></GrpNature><Peoples></Peoples><AppntOnWorkPeoples></AppntOnWorkPeoples><AppntOffWorkPeoples></AppntOffWorkPeoples><AppntOtherPeoples></AppntOtherPeoples><Peoples3>5</Peoples3><OnWorkPeoples></OnWorkPeoples><OffWorkPeoples></OffWorkPeoples><OtherPeoples></OtherPeoples><RelaPeoples></RelaPeoples><RelaMatePeoples></RelaMatePeoples><RelaYoungPeoples></RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>5</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>cc22</MainInsuredName><InsuredName>czh</InsuredName><Sex>1</Sex><Birthday>1972-09-06</Birthday><IDType>0</IDType><IDNo>320111197209064826</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>4</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>cc22</MainInsuredName><InsuredName>yst</InsuredName><Sex>0</Sex><Birthday>1965-10-15</Birthday><IDType>0</IDType><IDNo>320123196510150237</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>齐超</InsuredName><Sex>0</Sex><Birthday>1989-12-01</Birthday><IDType>0</IDType><IDNo>320123198912012031</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>任雅娣</InsuredName><Sex>1</Sex><Birthday>1988-11-28</Birthday><IDType>0</IDType><IDNo>320123198811283228</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>1</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>骆文娟</InsuredName><Sex>1</Sex><Birthday>1964-12-31</Birthday><IDType>0</IDType><IDNo>320123196412310622</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys>1</Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>M</InsuYearFlag><InsuYear>12</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>100000</FacValAmnt><Prem>51.0</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>10000</FacValAmnt><Prem>28.5</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1</RiskCode><DutyCode>null</DutyCode><FacValAmnt>9000</FacValAmnt><Prem>20.5</Prem><FacValAmntType>0</FacValAmntType></ITEM></WRAPPARAMLIST></GRPCONTDATA>";
	    	//String s3 = "sendXml=<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>BYW2017050417512144626</BatchNo><SendDate>2017-05-05</SendDate><SendTime>09:42:46</SendTime><SendOperator>HKXS</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-05-05</HandlerDate><AgentCom>PY002003004001</AgentCom><AgentCode>2232131416</AgentCode><FirstTrialOperator>gzh</FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-05-05</ReceiveDate><CoInsuranceFlag>1</CoInsuranceFlag><PayMode>1</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-05-10</CValiDate><CInValiDate>2018-05-09</CInValiDate><PeoplesInsu>5</PeoplesInsu><Remark></Remark><EjbAgent>86320000</EjbAgent></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>测试22</GrpName><Phone>888888</Phone><OrgancomCode>TEST-11</OrgancomCode><UnifiedSocialCreditNo></UnifiedSocialCreditNo><GrpAddress>cc2</GrpAddress><GrpZipCode></GrpZipCode><TaxNo></TaxNo><LegalPersonName>cc1</LegalPersonName><LegalPersonIDNo>112345678</LegalPersonIDNo><LinkMan1>cc1</LinkMan1><Phone1>888888</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType></BusinessType><BusinessBigType></BusinessBigType><GrpNature></GrpNature><Peoples></Peoples><AppntOnWorkPeoples></AppntOnWorkPeoples><AppntOffWorkPeoples></AppntOffWorkPeoples><AppntOtherPeoples></AppntOtherPeoples><Peoples3>5</Peoples3><OnWorkPeoples></OnWorkPeoples><OffWorkPeoples></OffWorkPeoples><OtherPeoples></OtherPeoples><RelaPeoples></RelaPeoples><RelaMatePeoples></RelaMatePeoples><RelaYoungPeoples></RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>5</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>cc22</MainInsuredName><InsuredName>czh</InsuredName><Sex>1</Sex><Birthday>1972-09-06</Birthday><IDType>0</IDType><IDNo>320111197209064826</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>4</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>cc22</MainInsuredName><InsuredName>yst</InsuredName><Sex>0</Sex><Birthday>1965-10-15</Birthday><IDType>0</IDType><IDNo>320123196510150237</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>齐超</InsuredName><Sex>0</Sex><Birthday>1989-12-01</Birthday><IDType>0</IDType><IDNo>320123198912012031</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>任雅娣</InsuredName><Sex>1</Sex><Birthday>1988-11-28</Birthday><IDType>0</IDType><IDNo>320123198811283228</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>1</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>骆文娟</InsuredName><Sex>1</Sex><Birthday>1964-12-31</Birthday><IDType>0</IDType><IDNo>320123196412310622</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys>1</Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>M</InsuYearFlag><InsuYear>12</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>100000</FacValAmnt><Prem>51.0</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>10000</FacValAmnt><Prem>28.5</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1</RiskCode><DutyCode>null</DutyCode><FacValAmnt>9000</FacValAmnt><Prem>20.5</Prem><FacValAmntType>0</FacValAmntType></ITEM></WRAPPARAMLIST></GRPCONTDATA>";
			System.out.println(sendPost("http://172.29.14.196:8080/T1/T1", "batch_no=1"));
		}
	}


