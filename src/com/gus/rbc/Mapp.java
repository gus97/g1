package com.gus.rbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Mapp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EcooperationWebServiceService ews = new EcooperationWebServiceService();
		EcooperationWebService em = ews.getPort(EcooperationWebService.class);
		String xml = 
				"<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?>\n" +
						"<ApplyInfo>\n" + 
						"  <GeneralInfo>\n" + 
						"    <UUID>gus100000011</UUID>\n" + 
						"    <PlateformCode>CPI000178</PlateformCode>\n" + 
						"    <Md5Value>2a30debbaa4e7cf03f1b8a6fff96a5ec</Md5Value>\n" + 
						"  </GeneralInfo>\n" + 
						"  <PolicyInfos>\n" + 
						"    <PolicyInfo>\n" + 
						"      <SerialNo>1</SerialNo>\n" + 
						"      <RiskCode>EAC</RiskCode>\n" + 
						"      <OperateTimes>2015-11-12 11:11:02</OperateTimes>\n" + 
						"      <StartDate>2017-03-18</StartDate>\n" + 
						"      <EndDate>2018-03-17</EndDate>\n" + 
						"      <StartHour>0</StartHour>\n" + 
						"      <EndHour>24</EndHour>\n" + 
						"      <SumAmount>90000.00</SumAmount>\n" + 
						"      <SumPremium>225.00</SumPremium>\n" + 
						"      <ArguSolution>1</ArguSolution>\n" + 
						"      <Quantity>1</Quantity>\n" + 
						"      <InsuredPlan>\n" + 
						"        <RationType>EAC0000001</RationType>\n" + 
						"        <Schemes>\n" + 
						"          <Scheme>\n" + 
						"            <SchemeCode>1</SchemeCode>\n" + 
						"            <SchemeAmount>30000.00</SchemeAmount>\n" + 
						"            <SchemePremium>15.00</SchemePremium>\n" + 
						"          </Scheme>\n" + 
						"          <Scheme>\n" + 
						"            <SchemeCode>2</SchemeCode>\n" + 
						"            <SchemeAmount>30000.00</SchemeAmount>\n" + 
						"            <SchemePremium>90.00</SchemePremium>\n" + 
						"          </Scheme>\n" + 
						"          <Scheme>\n" + 
						"            <SchemeCode>3</SchemeCode>\n" + 
						"            <SchemeAmount>30000.00</SchemeAmount>\n" + 
						"            <SchemePremium>120.00</SchemePremium>\n" + 
						"          </Scheme>\n" + 
						"        </Schemes>\n" + 
						"      </InsuredPlan>\n" + 
						"      <Applicant>\n" + 
						"        <AppliName>测试</AppliName>\n" + 
						"        <AppliIdType>04</AppliIdType>\n" + 
						"        <AppliIdNo>haha0618</AppliIdNo>\n" + 
						"        <AppliIdentity>1</AppliIdentity>\n" + 
						"      </Applicant>\n" + 
						"      <Insureds>\n" + 
						"        <Insured>\n" + 
						"          <InsuredSeqNo>1</InsuredSeqNo>\n" + 
						"          <InsuredName>测试</InsuredName>\n" + 
						"          <InsuredIdType>04</InsuredIdType>\n" + 
						"          <InsuredIdNo>haha0618</InsuredIdNo>\n" + 
						"          <InsuredBirthday>1997-08-27</InsuredBirthday>\n" + 
						"          <InsuredSex>1</InsuredSex>\n" + 
						"          <Occupation>5</Occupation>\n" + 
						"        </Insured>\n" + 
						"            <Insured>\n" + 
						"          <InsuredSeqNo>2</InsuredSeqNo>\n" + 
						"          <InsuredName>测试</InsuredName>\n" + 
						"          <InsuredIdType>04</InsuredIdType>\n" + 
						"          <InsuredIdNo>haha0618</InsuredIdNo>\n" + 
						"          <InsuredBirthday>1997-08-27</InsuredBirthday>\n" + 
						"          <InsuredSex>1</InsuredSex>\n" + 
						"          <Occupation>5</Occupation>\n" + 
						"        </Insured>\n" + 
						"            <Insured>\n" + 
						"          <InsuredSeqNo>3</InsuredSeqNo>\n" + 
						"          <InsuredName>测试</InsuredName>\n" + 
						"          <InsuredIdType>04</InsuredIdType>\n" + 
						"          <InsuredIdNo>haha0618</InsuredIdNo>\n" + 
						"          <InsuredBirthday>1997-08-27</InsuredBirthday>\n" + 
						"          <InsuredSex>1</InsuredSex>\n" + 
						"          <Occupation>5</Occupation>\n" + 
						"        </Insured>\n" + 
						"      </Insureds>\n" + 
						"    <ExtendInfos>\n" + 
						"    <ExtendInfo key=\"limit\">100</ExtendInfo>\n" + 
						"    </ExtendInfos>\n" + 
						"    </PolicyInfo>\n" + 
						"  </PolicyInfos>\n" + 
						"</ApplyInfo>";

		//System.out.println(MD5.md5("gus100000011"+"225.00"+"Picc37mu63ht38mw"));
		
		String a = em.insureService("001001", xml);
		
		System.out.println(a);
		
		@SuppressWarnings("unused")
		boolean t = true;
		
		Pattern p = Pattern.compile("<ErrorCode>(.*)</ErrorCode>");
		Matcher m = p.matcher(a);
		while (m.find()) {
			if(!m.group(1).equals("00")){
				t = false;
			}
		}
		
		//保单号
		p = Pattern.compile("<PolicyNo>(.*)</PolicyNo>");
		m = p.matcher(a);
		while (m.find()) {
			if(m.group(1).equals("")){
				t = false;
			}
		}
		
		p = Pattern.compile("<SaveResult>(.*)</SaveResult>");
		m = p.matcher(a);
		while (m.find()) {
			if(!m.group(1).equals("00")){
				t = false;
			}
		}
		
		System.out.println("123456".indexOf("456"));
	}
}
