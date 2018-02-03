package com.bx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

public class Mapp {

	public static void main(String[] args) throws ClientProtocolException, IOException {

		String s = "<GRPCONTDATA>" + "<MSGHEAD>" + "<ITEM>" + "<BatchNo>GEJBWX_GRP01201418033520026</BatchNo>"
				+ "<SendDate>2017-3-1</SendDate>" + "<SendTime>15:52:31</SendTime>"
				+ "<SendOperator>GEJB</SendOperator>" + "<MsgType>WX_GRP06</MsgType>" + "</ITEM>" + "</MSGHEAD>"
				+ "<GRPCONTLIST>" + "<ITEM>" + "<PrtNo>1</PrtNo>" + "<ManageCom>86320000</ManageCom>"
				+ "<SaleChnl>03</SaleChnl>" + "<HandlerDate>2017-3-1</HandlerDate>" + "<AgentCom></AgentCom>"
				+ "<AgentCode>3202000028</AgentCode>" + "<FirstTrialOperator></FirstTrialOperator>"
				+ "<MarketType>1</MarketType>" + "<ReceiveDate>2017-3-1</ReceiveDate>"
				+ "<CoInsuranceFlag>0</CoInsuranceFlag>" + "<PayMode>4</PayMode>" + "<BankCode></BankCode>"
				+ "<BankAccNo></BankAccNo>" + "<AccName></AccName>" + "<PayIntv>0</PayIntv>"
				+ "<CValiDate>2017-3-1</CValiDate>" + "<CInValiDate></CInValiDate>"
				+ "<PeoplesInsu>3</PeoplesInsu>" + "<Remark>BBB</Remark>" + "</ITEM>" + "</GRPCONTLIST>"
				+ "<GRPAPPNTLIST>" + "<ITEM>" + "<GrpNo>1</GrpNo>" + "<GrpName>郑州晚报</GrpName><Pho"
				+ "ne>0371-25668532</Phone>" + "<OrgancomCode>0205</OrgancomCode>" + "<GrpAddress></GrpAddress>"
				+ "<GrpZipCode>121275</GrpZipCode>" + "<TaxNo>02031</TaxNo>"
				+ "<LegalPersonName>张琳</LegalPersonName><L" + "egalPersonIDNo>10210014</LegalPersonIDNo>"
				+ "<LinkMan1>张琳</LinkMan1><P" + "hone1>28510003</Phone1>" + "<Fax1></Fax1>"
				+ "<E_Mail1></E_Mail1>" + "<BusinessType>009</BusinessType>"
				+ "<BusinessBigType>新闻</BusinessBigType><G" + "rpNature>09</GrpNature>" + "<Peoples>3</Peoples>"
				+ "<AppntOnWorkPeoples>3</AppntOnWorkPeoples>" + "<AppntOffWorkPeoples>0</AppntOffWorkPeoples>"
				+ "<AppntOtherPeoples>0</AppntOtherPeoples>" + "<Peoples3>3</Peoples3>"
				+ "<OnWorkPeoples>3</OnWorkPeoples>" + "<OffWorkPeoples>0</OffWorkPeoples>"
				+ "<OtherPeoples>0</OtherPeoples>" + "<RelaPeoples>0</RelaPeoples>"
				+ "<RelaMatePeoples>0</RelaMatePeoples>" + "<RelaYoungPeoples>0</RelaYoungPeoples>" + "</ITEM>"
				+ "</GRPAPPNTLIST>" + "<GRPINSULIST>" + "<ITEM>" + "<InsuredID>1</InsuredID>"
				+ "<ContID>1</ContID>" + "<Retire>1</Retire>" + "<RelationToInsured>00</RelationToInsured>"
				+ "<MainInsuredName>谢娜44</MainInsuredName><I" + "nsuredName>谢娜44</InsuredName><S" + "ex>1</Sex>"
				+ "<Birthday>1984-3-27</Birthday>" + "<IDType>4</IDType>" + "<IDNo>19840327</IDNo>"
				+ "<ContPlanCode></ContPlanCode>" + "<OccupationType>1</OccupationType>"
				+ "<OccupationCode>05802</OccupationCode>" + "<BankCode></BankCode>" + "<BankAccNo></BankAccNo>"
				+ "<AccName></AccName>" + "<Phone></Phone>" + "</ITEM>" + "<ITEM>"
				+ "<InsuredID>2</InsuredID>" + "<ContID>1</ContID>" + "<Retire>1</Retire>"
				+ "<RelationToInsured>26</RelationToInsured>" + "<MainInsuredName>谢娜44</MainInsuredName><I"
				+ "nsuredName>度海涛44</InsuredName><Se" + "x>0</Sex>" + "<Birthday>1983-03-25</Birthday>"
				+ "<IDType>4</IDType>" + "<IDNo>19830305</IDNo>" + "<ContPlanCode></ContPlanCode>"
				+ "<OccupationType>1</OccupationType>" + "<OccupationCode>05802</OccupationCode>"
				+ "<BankCode></BankCode>" + "<BankAccNo></BankAccNo>" + "<AccName></AccName>"
				+ "<Phone></Phone>" + "</ITEM>" + "<ITEM>" + "<InsuredID>3</InsuredID>" + "<ContID>1</ContID>"
				+ "<Retire>1</Retire>" + "<RelationToInsured>26</RelationToInsured>"
				+ "<MainInsuredName>谢娜44</MainInsuredName><I" + "nsuredName>庾澄庆44</InsuredName><Se" + "x>0</Sex>"
				+ "<Birthday>1970-5-21</Birthday>" + "<IDType>4</IDType>" + "<IDNo>123836ht0</IDNo>"
				+ "<ContPlanCode></ContPlanCode>" + "<OccupationType>1</OccupationType>"
				+ "<OccupationCode>05802</OccupationCode>" + "<BankCode></BankCode>" + "<BankAccNo></BankAccNo>"
				+ "<AccName></AccName>" + "<Phone></Phone>" + "</ITEM>" + "</GRPINSULIST>" + "<GRPWRAPLIST>"
				+ "<ITEM>" + "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<MainRiskCode>560801</MainRiskCode>"
				+ "<Amnt></Amnt>" + "<Prem></Prem>" + "<Mult></Mult>" + "<Copys></Copys>"
				+ "<PayEndYearFlag></PayEndYearFlag>" + "<PayEndYear></PayEndYear>"
				+ "<InsuYearFlag>Y</InsuYearFlag>" + "<InsuYear>1</InsuYear>" + "</ITEM>" + "</GRPWRAPLIST>"
				+ "<WRAPPARAMLIST>" + "<ITEM>" + "<RiskWrapCode>ZZJ078</RiskWrapCode>"
				+ "<RiskCode>560801</RiskCode>" + "<DutyCode>684001</DutyCode>"
				+ "<FacValAmnt>800000</FacValAmnt>" + "<FacValAmntType>0</FacValAmntType>" + "<Prem>51</Prem>"
				+ "</ITEM>" + "<ITEM>" + "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>1607</RiskCode>"
				+ "<DutyCode>614002</DutyCode>" + "<FacValAmnt>80000</FacValAmnt>"
				+ "<FacValAmntType>0</FacValAmntType>" + "<Prem>28.5</Prem>" + "</ITEM>" + "<ITEM>"
				+ "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>1601</RiskCode>"
				+ "<DutyCode>601001</DutyCode>" + "<FacValAmnt>200</FacValAmnt>"
				+ "<FacValAmntType>1</FacValAmntType>" + "<Prem>20.5</Prem>" + "</ITEM>" + "<ITEM>"
				+ "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>551101</RiskCode>"
				+ "<DutyCode>685001</DutyCode>" + "<FacValAmnt>500000</FacValAmnt>"
				+ "<FacValAmntType>0</FacValAmntType>" + "<Prem>3</Prem>" + "</ITEM>" + "<ITEM>"
				+ "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>551101</RiskCode>"
				+ "<DutyCode>685002</DutyCode>" + "<FacValAmnt>200000</FacValAmnt>"
				+ "<FacValAmntType>0</FacValAmntType>" + "<Prem>4</Prem>" + "</ITEM>" + "<ITEM>"
				+ "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>551101</RiskCode>"
				+ "<DutyCode>685003</DutyCode>" + "<FacValAmnt>200000</FacValAmnt>"
				+ "<FacValAmntType>0</FacValAmntType>" + "<Prem>4</Prem>" + "</ITEM>" + "<ITEM>"
				+ "<RiskWrapCode>ZZJ078</RiskWrapCode>" + "<RiskCode>551101</RiskCode>"
				+ "<DutyCode>685004</DutyCode>" + "<FacValAmnt>100000</FacValAmnt>"
				+ "<FacValAmntType>0</FacValAmntType>" + "<Prem>6</Prem>" + "</ITEM>" + "</WRAPPARAMLIST>"
				+ "</GRPCONTDATA>";
		
	//	URL u = new URL("http://124.127.37.57:8088/hk?sendXml=<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>GEJBWX_GRP01201418033520026</BatchNo><SendDate>2017-3-1</SendDate><SendTime>15:52:31</SendTime><SendOperator>GEJB</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-3-1</HandlerDate><AgentCom></AgentCom><AgentCode>3202000028</AgentCode><FirstTrialOperator></FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-3-1</ReceiveDate><CoInsuranceFlag>0</CoInsuranceFlag><PayMode>4</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-3-1</CValiDate><CInValiDate></CInValiDate><PeoplesInsu>3</PeoplesInsu><Remark>BBB</Remark></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>郑州晚报</GrpName><Phone>0371-25668532</Phone><OrgancomCode>0205</OrgancomCode><GrpAddress></GrpAddress><GrpZipCode>121275</GrpZipCode><TaxNo>02031</TaxNo><LegalPersonName>张琳</LegalPersonName><LegalPersonIDNo>10210014</LegalPersonIDNo><LinkMan1>张琳</LinkMan1><Phone1>28510003</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType>009</BusinessType><BusinessBigType>新闻</BusinessBigType><GrpNature>09</GrpNature><Peoples>3</Peoples><AppntOnWorkPeoples>3</AppntOnWorkPeoples><AppntOffWorkPeoples>0</AppntOffWorkPeoples><AppntOtherPeoples>0</AppntOtherPeoples><Peoples3>3</Peoples3><OnWorkPeoples>3</OnWorkPeoples><OffWorkPeoples>0</OffWorkPeoples><OtherPeoples>0</OtherPeoples><RelaPeoples>0</RelaPeoples><RelaMatePeoples>0</RelaMatePeoples><RelaYoungPeoples>0</RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>1</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>00</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>谢娜44</InsuredName><Sex>1</Sex><Birthday>1984-3-27</Birthday><IDType>4</IDType><IDNo>19840327</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>度海涛44</InsuredName><Sex>0</Sex><Birthday>1983-03-25</Birthday><IDType>4</IDType><IDNo>19830305</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>xx44</MainInsuredName><InsuredName>庾澄庆44</InsuredName><Sex>0</Sex><Birthday>1970-5-21</Birthday><IDType>4</IDType><IDNo>123836ht0</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys></Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>Y</InsuYearFlag><InsuYear>1</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>800000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>51</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>80000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>28.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1601</RiskCode><DutyCode>601001</DutyCode><FacValAmnt>200</FacValAmnt><FacValAmntType>1</FacValAmntType><Prem>20.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>500000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>3</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685002</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685003</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685004</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>6</Prem></ITEM></WRAPPARAMLIST></GRPCONTDATA>");

		s = "<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>GEJBWX_GRP01201418033520026</BatchNo><SendDate>2017-3-1</SendDate><SendTime>15:52:31</SendTime><SendOperator>GEJB</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-3-1</HandlerDate><AgentCom></AgentCom><AgentCode>3202000028</AgentCode><FirstTrialOperator></FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-3-1</ReceiveDate><CoInsuranceFlag>0</CoInsuranceFlag><PayMode>4</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-3-1</CValiDate><CInValiDate></CInValiDate><PeoplesInsu>3</PeoplesInsu><Remark>BBB</Remark></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>郑州晚报</GrpName><Phone>0371-25668532</Phone><OrgancomCode>0205</OrgancomCode><GrpAddress></GrpAddress><GrpZipCode>121275</GrpZipCode><TaxNo>02031</TaxNo><LegalPersonName>张琳</LegalPersonName><LegalPersonIDNo>10210014</LegalPersonIDNo><LinkMan1>张琳</LinkMan1><Phone1>28510003</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType>009</BusinessType><BusinessBigType>新闻</BusinessBigType><GrpNature>09</GrpNature><Peoples>3</Peoples><AppntOnWorkPeoples>3</AppntOnWorkPeoples><AppntOffWorkPeoples>0</AppntOffWorkPeoples><AppntOtherPeoples>0</AppntOtherPeoples><Peoples3>3</Peoples3><OnWorkPeoples>3</OnWorkPeoples><OffWorkPeoples>0</OffWorkPeoples><OtherPeoples>0</OtherPeoples><RelaPeoples>0</RelaPeoples><RelaMatePeoples>0</RelaMatePeoples><RelaYoungPeoples>0</RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>1</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>00</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>谢娜44</InsuredName><Sex>1</Sex><Birthday>1984-3-27</Birthday><IDType>4</IDType><IDNo>19840327</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>度海涛44</InsuredName><Sex>0</Sex><Birthday>1983-03-25</Birthday><IDType>4</IDType><IDNo>19830305</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>xx44</MainInsuredName><InsuredName>庾澄庆44</InsuredName><Sex>0</Sex><Birthday>1970-5-21</Birthday><IDType>4</IDType><IDNo>123836ht0</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys></Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>Y</InsuYearFlag><InsuYear>1</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>800000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>51</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>80000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>28.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1601</RiskCode><DutyCode>601001</DutyCode><FacValAmnt>200</FacValAmnt><FacValAmntType>1</FacValAmntType><Prem>20.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>500000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>3</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685002</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685003</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685004</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>6</Prem></ITEM></WRAPPARAMLIST></GRPCONTDATA>";
		System.out.println(sendGet("http://124.127.37.57:8088/hk", s));
				
				//CloseableHttpClient httpclient = HttpClients.createDefault();
		//HttpPost httppost = new HttpPost("http://124.127.37.57:8088/hk?sendXml=<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>GEJBWX_GRP01201418033520026</BatchNo><SendDate>2017-3-1</SendDate><SendTime>15:52:31</SendTime><SendOperator>GEJB</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-3-1</HandlerDate><AgentCom></AgentCom><AgentCode>3202000028</AgentCode><FirstTrialOperator></FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-3-1</ReceiveDate><CoInsuranceFlag>0</CoInsuranceFlag><PayMode>4</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-3-1</CValiDate><CInValiDate></CInValiDate><PeoplesInsu>3</PeoplesInsu><Remark>BBB</Remark></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>郑州晚报</GrpName><Phone>0371-25668532</Phone><OrgancomCode>0205</OrgancomCode><GrpAddress></GrpAddress><GrpZipCode>121275</GrpZipCode><TaxNo>02031</TaxNo><LegalPersonName>张琳</LegalPersonName><LegalPersonIDNo>10210014</LegalPersonIDNo><LinkMan1>张琳</LinkMan1><Phone1>28510003</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType>009</BusinessType><BusinessBigType>新闻</BusinessBigType><GrpNature>09</GrpNature><Peoples>3</Peoples><AppntOnWorkPeoples>3</AppntOnWorkPeoples><AppntOffWorkPeoples>0</AppntOffWorkPeoples><AppntOtherPeoples>0</AppntOtherPeoples><Peoples3>3</Peoples3><OnWorkPeoples>3</OnWorkPeoples><OffWorkPeoples>0</OffWorkPeoples><OtherPeoples>0</OtherPeoples><RelaPeoples>0</RelaPeoples><RelaMatePeoples>0</RelaMatePeoples><RelaYoungPeoples>0</RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>1</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>00</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>谢娜44</InsuredName><Sex>1</Sex><Birthday>1984-3-27</Birthday><IDType>4</IDType><IDNo>19840327</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>谢娜44</MainInsuredName><InsuredName>度海涛44</InsuredName><Sex>0</Sex><Birthday>1983-03-25</Birthday><IDType>4</IDType><IDNo>19830305</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID>1</ContID><Retire>1</Retire><RelationToInsured>26</RelationToInsured><MainInsuredName>xx44</MainInsuredName><InsuredName>庾澄庆44</InsuredName><Sex>0</Sex><Birthday>1970-5-21</Birthday><IDType>4</IDType><IDNo>123836ht0</IDNo><ContPlanCode></ContPlanCode><OccupationType>1</OccupationType><OccupationCode>05802</OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys></Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>Y</InsuYearFlag><InsuYear>1</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>800000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>51</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>80000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>28.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1601</RiskCode><DutyCode>601001</DutyCode><FacValAmnt>200</FacValAmnt><FacValAmntType>1</FacValAmntType><Prem>20.5</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>500000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>3</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685002</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685003</DutyCode><FacValAmnt>200000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>4</Prem></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685004</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>6</Prem></ITEM></WRAPPARAMLIST></GRPCONTDATA>");
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("sendXml",s));
//		System.out.println(s);
//		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
//		
		//httppost.setEntity(uefEntity);
//		HttpResponse response  = httpclient.execute(httppost);
//		
//		HttpEntity resEntity = response.getEntity();
//		InputStreamReader reader2 = new InputStreamReader(resEntity.getContent(), "UTF-8");
//		char[] buff = new char[1024];
//		int length = 0;
//		String t = "";
//		while ((length = reader2.read(buff)) != -1) {
//			t += new String(buff, 0, length);
//		}
//		System.out.println(t + "=========");

	}
	
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
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
	                    connection.getInputStream()));
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
	        return result;
	    }

}
