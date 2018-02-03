package com.gopay.utils;

import java.util.Calendar;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

public class EJBAxis2MethodTest
{
    private static String tStrTargetEendPoint = "http://124.127.37.57:8088/gejb";
    
    private static String tStrNamespace = "http://service.grp.ECPH.com";
    
    //    private static String tStrTargetEendPoint = "http://114.247.172.148/gejb";
    //
    //    private static String tStrNamespace = "http://service.grp.ECPH.com";
    
    public static String Axis2MethodSend(OMElement Om)
    {
        String p = null;
        try
        {
            
            RPCServiceClient client = new RPCServiceClient();
            System.out.println("1111111111" + Om.toString());
            EndpointReference erf = new EndpointReference(tStrTargetEendPoint);
            Options option = client.getOptions();
            option.setTo(erf);
            option.setTimeOutInMilliSeconds(10000000000L);
            option.setAction("service");
            QName name = new QName(tStrNamespace, "service");
            Object[] object = new Object[] {Om.toString()};
            Class[] returnTypes = new Class[] {String.class};
            Object[] response = client.invokeBlocking(name, object, returnTypes);
            p = (String)response[0];
            System.out.println("core back:" + p);
        }
        catch (AxisFault e)
        {
            e.printStackTrace();
        }
        return p;
    }
    
    public static OMElement addOm(OMElement Om, String Name, String Value)
    {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement tName = factory.createOMElement(Name, null);
        tName.setText(Value);
        Om.addChild(tName);
        return Om;
    }
    
    /**
     * �����κ�
     */
    public static String makeBatchNO()
    {
        Calendar CD = Calendar.getInstance();
        int YY = CD.get(Calendar.YEAR);
        int MM = CD.get(Calendar.MONTH) + 1;
        int DD = CD.get(Calendar.DATE);
        int HH = CD.get(Calendar.HOUR_OF_DAY);
        int NN = CD.get(Calendar.MINUTE);
        int SS = CD.get(Calendar.SECOND);
        int MI = CD.get(Calendar.MILLISECOND);
        return "" + YY + MM + DD + HH + NN + SS + MI + genRandomNum(4);
    }
    
    /**
     * ����漴����
     * @param pwd_len ��ɵ�������ܳ���
     * @return  ������ַ�
     */
    private static String genRandomNum(int pwd_len)
    {
        int i; //��ɵ������
        int count = 0; //��ɵ�����ĳ���
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        while (count < pwd_len)
        {
            i = (int)(Math.random() * 10);
            if (i >= 0 && i < str.length)
            {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }
    
    public static String Axis2MethodSend(String Om)
    {
        //        tStrTargetEendPoint = "http://114.247.172.148/gejb";
        //    	tStrNamespace="http://webservice.sinosoft.com";
        String p = null;
        try
        {
            RPCServiceClient client = new RPCServiceClient();
            
            EndpointReference erf = new EndpointReference(tStrTargetEendPoint);
            Options option = client.getOptions();
            option.setTo(erf);
            option.setManageSession(true);
            option.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, false);
            option.setAction("service");
            option.setTimeOutInMilliSeconds(600000L);
            //            option.setProperty(HTTPConstants.CONNECTION_TIMEOUT, 900000);
            //            option.setProperty(HTTPConstants.SO_TIMEOUT, 900000);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT, new Integer(600000));
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT, new Integer(600000));
            
            //            option.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
            
            //            client.getOptions().setTo(erf);
            //            client.getOptions().setAction("service");
            //            client.getOptions().setTimeOutInMilliSeconds(600000L);
            //            client.getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
            //            client.getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, 600000);
            //            client.getOptions().setProperty(HTTPConstants.SO_TIMEOUT, 600000);
            QName name = new QName(tStrNamespace, "service");
            Object[] object = new Object[] {Om};
            Class[] returnTypes = new Class[] {String.class};
            
            Object[] response = client.invokeBlocking(name, object, returnTypes);
            p = (String)response[0];
            client.cleanupTransport();
            client.cleanup();
            System.out.println("返回报文：" + p);
            
        }
        catch (AxisFault e)
        {
            e.printStackTrace();
        }
        
        return p;
    }
    
    public static void main(String[] args)
    {
        //    	String a=
        //    	"<DataSet>"+
        //    		"<MsgHead>"+
        //        		"<BatchNo>1</BatchNo>"+
        //        		"<SendDate>2016-11-22</SendDate>"+
        //        		"<SendTime>10:29:14</SendTime>"+
        //        		"<BranchCode>WX</BranchCode>"+
        //        		"<SendOperator>WX</SendOperator>"+
        //        		"<MsgType>HM020</MsgType>"+
        //        	"</MsgHead>"+
        //        	"<ServiceResult>"+
        //    	 		"<ConsultID>1</ConsultID>"+
        //    	 		"<EvaluationDate>2016-11-22</EvaluationDate>"+
        //    	 		"<EvaluationTime>10:29:14</EvaluationTime>"+
        //    	 		"<Items>"+
        //    	 			"<Item>A</Item>"+
        //    	 			"<Score>1</Score>"+
        //    	        "</Items>"+
        //    	  "</ServiceResult>"+
        //       " </DataSet>";
        String a =
            "<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>BYW2017112413321047813</BatchNo><SendDate>2017-11-24</SendDate><SendTime>13:32:27</SendTime><SendOperator>HKXS</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86420000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-11-24</HandlerDate><AgentCom>PY015005101</AgentCom><AgentCode>6103000002</AgentCode><FirstTrialOperator>gzh</FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-11-24</ReceiveDate><CoInsuranceFlag>1</CoInsuranceFlag><PayMode>1</PayMode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-11-25</CValiDate><CInValiDate>2018-11-24</CInValiDate><PeoplesInsu>5</PeoplesInsu><Remark></Remark><EjbAgent>测试中介公司</EjbAgent></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>华凯普5</GrpName><Phone>13912345678</Phone><OrgancomCode>TEST-5</OrgancomCode><UnifiedSocialCreditNo></UnifiedSocialCreditNo><GrpAddress>华凯普5</GrpAddress><GrpZipCode></GrpZipCode><TaxNo></TaxNo><LegalPersonName></LegalPersonName><LegalPersonIDNo></LegalPersonIDNo><LinkMan1>华凯普5</LinkMan1><Phone1>13912345678</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType></BusinessType><BusinessBigType></BusinessBigType><GrpNature></GrpNature><Peoples></Peoples><AppntOnWorkPeoples></AppntOnWorkPeoples><AppntOffWorkPeoples></AppntOffWorkPeoples><AppntOtherPeoples></AppntOtherPeoples><Peoples3>5</Peoples3><OnWorkPeoples></OnWorkPeoples><OffWorkPeoples></OffWorkPeoples><OtherPeoples></OtherPeoples><RelaPeoples></RelaPeoples><RelaMatePeoples></RelaMatePeoples><RelaYoungPeoples></RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>5</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>00</RelationToInsured><MainInsuredName>万三阳</MainInsuredName><InsuredName>万三阳</InsuredName><Sex>1</Sex><Birthday>1984-10-29</Birthday><IDType>0</IDType><IDNo>410222198410295502</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>5</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>4</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>万三阳</MainInsuredName><InsuredName>沈童</InsuredName><Sex>1</Sex><Birthday>1991-02-19</Birthday><IDType>0</IDType><IDNo>371200199102193621</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>5</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>万三阳</MainInsuredName><InsuredName>曹明</InsuredName><Sex>0</Sex><Birthday>1983-01-17</Birthday><IDType>0</IDType><IDNo>321084198301171938</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>5</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>2</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>万三阳</MainInsuredName><InsuredName>范亮</InsuredName><Sex>1</Sex><Birthday>1989-11-10</Birthday><IDType>0</IDType><IDNo>652929198911102827</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>5</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>1</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>万三阳</MainInsuredName><InsuredName>张尊国</InsuredName><Sex>0</Sex><Birthday>1979-07-07</Birthday><IDType>0</IDType><IDNo>140401197907073357</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>5</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ080</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt></Amnt><Prem></Prem><Mult></Mult><Copys>1</Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>M</InsuYearFlag><InsuYear>12</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>100000</FacValAmnt><Prem>187.0</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>10000</FacValAmnt><Prem>38.5</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>100000</FacValAmnt><Prem>3.0</Prem><FacValAmntType>0</FacValAmntType></ITEM></WRAPPARAMLIST></GRPCONTDATA>";
        System.out.println("输入报文：" + a);
        EJBAxis2MethodTest.Axis2MethodSend(a);
    }
}
