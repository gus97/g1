package com.gopay.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings({"rawtypes"})
public class RMBX
{
    public static String rmbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid)
        throws UnsupportedEncodingException
    {
        List info =
            jdbcTemplate.queryForList("SELECT interuser,interpwd,CODE FROM insproject WHERE id = ? ",
                new Object[] {Integer.valueOf(projectid)});

        if ((info == null) || (info.size() == 0))
        {
            return "方案号错误";
        }

        Date d = new Date();
        //        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat r = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat t = new SimpleDateFormat("HH:mm:ss");
        String r1 = r.format(d);
        String t1 = t.format(d);

        StringBuffer buff_xml = new StringBuffer();

        buff_xml.append("<GRPCONTDATA>");
        buff_xml.append("<MSGHEAD>");
        buff_xml.append("<ITEM>");
        buff_xml.append("<BatchNo>" + BatchNo + "</BatchNo>");
        buff_xml.append("<SendDate>" + r1 + "</SendDate>");
        buff_xml.append("<SendTime>" + t1 + "</SendTime>");
        buff_xml.append("<SendOperator>HKXS</SendOperator>");
        buff_xml.append("<MsgType>WX_GRP06</MsgType>");
        buff_xml.append("</ITEM>");
        buff_xml.append("</MSGHEAD>");

        List teamorderList =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] {BatchNo});

        Map m10 =
            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND  insid = 10 AND kind=4",
                new Object[] {Integer.valueOf(medid)});

        buff_xml.append("<GRPCONTLIST>");
        buff_xml.append("<ITEM>");
        buff_xml.append("<PrtNo>1</PrtNo>");
        buff_xml.append("<ManageCom>" + m10.get("agentcode") + "</ManageCom>");
        buff_xml.append("<SaleChnl>03</SaleChnl>");
        buff_xml.append("<HandlerDate>" + r1 + "</HandlerDate>");
        if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 10 AND kind=1") == 0)
        {
            return "AgentCom error";
        }
        if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 10 AND kind=2") == 0)
        {
            return "AgentCode error";
        }
        Map m =
            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND  insid = 10 AND kind = 1",
                new Object[] {Integer.valueOf(medid)});

        Map m1 =
            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany  WHERE toinscompany.syscode= '出单员B' AND  insid = 10 AND kind= 2");

        buff_xml.append("<AgentCom>" + m.get("agentcode") + "</AgentCom>");
        buff_xml.append("<AgentCode>" + m1.get("agentcode") + "</AgentCode>");
        buff_xml.append("<FirstTrialOperator>gzh</FirstTrialOperator>");
        buff_xml.append("<MarketType>1</MarketType>");
        buff_xml.append("<ReceiveDate>" + r1 + "</ReceiveDate>");
        buff_xml.append("<CoInsuranceFlag>1</CoInsuranceFlag>");
        buff_xml.append("<PayMode>1</PayMode>");
        buff_xml.append("<BankCode></BankCode>");
        buff_xml.append("<BankAccNo></BankAccNo>");
        buff_xml.append("<AccName></AccName>");
        buff_xml.append("<PayIntv>0</PayIntv>");

        buff_xml.append("<CValiDate>" + ((Map)teamorderList.get(0)).get("CValiDate").toString().substring(0, 10)
            + "</CValiDate>");
        buff_xml.append("<CInValiDate>" + ((Map)teamorderList.get(0)).get("CInValiDate").toString().substring(0, 10)
            + "</CInValiDate>");
        buff_xml.append("<PeoplesInsu>" + ((Map)teamorderList.get(0)).get("PeoplesInsu") + "</PeoplesInsu>");
        buff_xml.append("<Remark>"
            + (((Map)teamorderList.get(0)).get("Remark") == null ? "" : ((Map)teamorderList.get(0)).get("Remark"))
            + "</Remark>");
        if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 10 AND kind=4") == 0)
        {
            return "EjbAgent error";
        }
        //        Map m2 =
        //            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND  insid = 10 AND kind = 4",
        //                new Object[] {Integer.valueOf(medid)});

        Map m3 =
            jdbcTemplate.queryForMap("SELECT name FROM medcompany  WHERE medcompany.id = ?",
                new Object[] {Integer.valueOf(medid)});

        buff_xml.append("<EjbAgent>" + m3.get("name") + "</EjbAgent>");
        buff_xml.append("</ITEM>");
        buff_xml.append("</GRPCONTLIST>");

        buff_xml.append("<GRPAPPNTLIST>");
        buff_xml.append("<ITEM>");
        buff_xml.append("<GrpNo>1</GrpNo>");
        buff_xml.append("<GrpName>" + ((Map)teamorderList.get(0)).get("GrpName") + "</GrpName>");
        buff_xml.append("<Phone>" + ((Map)teamorderList.get(0)).get("Phone") + "</Phone>");
        String organcomCode = (String)((Map)teamorderList.get(0)).get("organcomCode");
        //        String taxNo = (String)((Map)teamorderList.get(0)).get("taxNo");
        //        String businessCode = (String)((Map)teamorderList.get(0)).get("businessCode");
        //        String CreditCode = (String)((Map)teamorderList.get(0)).get("CreditCode");
        String s1 = "";
        if ((organcomCode != null) && (!"--".equals(organcomCode)) && (!"".equals(organcomCode)))
        {
            s1 = organcomCode;
        }

        String a1 = "";
        String a2 = "";
        if ((((Map)teamorderList.get(0)).get("TaxNo") != null)
            && (((Map)teamorderList.get(0)).get("TaxNo").toString().equals("--")))
        {
            a1 = "";
        }
        else
        {
            a1 = (String)((Map)teamorderList.get(0)).get("TaxNo");
        }
        if ((((Map)teamorderList.get(0)).get("CreditCode") != null)
            && (((Map)teamorderList.get(0)).get("CreditCode").toString().equals("--")))
        {
            a2 = "";
        }
        else
        {
            a2 = (String)((Map)teamorderList.get(0)).get("CreditCode");
        }

        buff_xml.append("<OrgancomCode>" + s1 + "</OrgancomCode>");
        buff_xml.append("<UnifiedSocialCreditNo>" + a2 + "</UnifiedSocialCreditNo>");
        buff_xml.append("<GrpAddress>" + ((Map)teamorderList.get(0)).get("GrpAddress") + "</GrpAddress>");
        buff_xml.append("<GrpZipCode>"
            + (((Map)teamorderList.get(0)).get("GrpZipCode") == null ? ""
                : ((Map)teamorderList.get(0)).get("GrpZipCode")) + "</GrpZipCode>");
        buff_xml.append("<TaxNo>" + a1 + "</TaxNo>");
        buff_xml.append("<LegalPersonName></LegalPersonName>");
        buff_xml.append("<LegalPersonIDNo></LegalPersonIDNo>");
        buff_xml.append("<LinkMan1>" + ((Map)teamorderList.get(0)).get("LinkMan1") + "</LinkMan1>");
        buff_xml.append("<Phone1>" + ((Map)teamorderList.get(0)).get("Phone1") + "</Phone1>");
        buff_xml.append("<Fax1></Fax1>");
        buff_xml.append("<E_Mail1></E_Mail1>");
        buff_xml.append("<BusinessType></BusinessType>");
        buff_xml.append("<BusinessBigType></BusinessBigType>");
        buff_xml.append("<GrpNature></GrpNature>");
        buff_xml.append("<Peoples></Peoples>");
        buff_xml.append("<AppntOnWorkPeoples></AppntOnWorkPeoples>");
        buff_xml.append("<AppntOffWorkPeoples></AppntOffWorkPeoples>");
        buff_xml.append("<AppntOtherPeoples></AppntOtherPeoples>");
        buff_xml.append("<Peoples3>" + ((Map)teamorderList.get(0)).get("PeoplesInsu") + "</Peoples3>");
        buff_xml.append("<OnWorkPeoples></OnWorkPeoples>");
        buff_xml.append("<OffWorkPeoples></OffWorkPeoples>");
        buff_xml.append("<OtherPeoples></OtherPeoples>");
        buff_xml.append("<RelaPeoples></RelaPeoples>");
        buff_xml.append("<RelaMatePeoples></RelaMatePeoples>");
        buff_xml.append("<RelaYoungPeoples></RelaYoungPeoples>");
        buff_xml.append("</ITEM>");
        buff_xml.append("</GRPAPPNTLIST>");

        buff_xml.append("<GRPINSULIST>");
        List<Map<String, Object>> picc_teamperson_bb =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ?  and RelationToInsured <> '00' order by id desc",
                new Object[] {BatchNo});

        String r2;
        int i = 0;
        String MainInsuredName = "";
        for (Map map : picc_teamperson_bb)
        {
            r2 = (String)map.get("RelationToInsured");
            String InsuredName = (String)map.get("InsuredName");

            if (map.get("MainInsuredName").toString().equals(map.get("InsuredName").toString()))
            {
                r2 = "00";
            }
            
            if (i == 0)
            {
                r2 = "00";
                MainInsuredName = (String)map.get("InsuredName");
            }

            buff_xml.append("<ITEM>");
            buff_xml.append("<InsuredID>"
                + (Integer.parseInt(new StringBuilder().append(map.get("InsuredID")).toString()) - 1) + "</InsuredID>");
            buff_xml.append("<ContID></ContID>");
            buff_xml.append("<Retire></Retire>");
            buff_xml.append("<RelationToInsured>" + r2 + "</RelationToInsured>");
            //            buff_xml.append("<MainInsuredName>" + map.get("MainInsuredName") + "</MainInsuredName>");
            //            buff_xml.append("<InsuredName>" + map.get("InsuredName") + "</InsuredName>");
            buff_xml.append("<MainInsuredName>" + MainInsuredName + "</MainInsuredName>");
            buff_xml.append("<InsuredName>" + InsuredName + "</InsuredName>");
            buff_xml.append("<Sex>" + map.get("sex") + "</Sex>");
            buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");
            buff_xml.append("<IDType>" + map.get("IDType") + "</IDType>");
            buff_xml.append("<IDNo>" + map.get("IDNo") + "</IDNo>");
            buff_xml.append("<ContPlanCode>" + map.get("ContPlanCode") + "</ContPlanCode>");
            buff_xml.append("<OccupationType>" + map.get("personid") + "</OccupationType>");
            buff_xml.append("<OccupationCode></OccupationCode>");
            buff_xml.append("<BankCode></BankCode>");
            buff_xml.append("<BankAccNo></BankAccNo>");
            buff_xml.append("<AccName></AccName>");
            buff_xml.append("<Phone></Phone>");
            buff_xml.append("</ITEM>");
            
            i++;
        }
        buff_xml.append("</GRPINSULIST>");

        List<Map<String, Object>> picc_plan_info =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo  = ?", new Object[] {BatchNo});

        buff_xml.append("<GRPWRAPLIST>");
        for (Map map : picc_plan_info)
        {
            buff_xml.append("<ITEM>");
            buff_xml.append("<RiskWrapCode>" + map.get("RiskWrapCode") + "</RiskWrapCode>");
            buff_xml.append("<MainRiskCode>" + map.get("MainRiskCode") + "</MainRiskCode>");

            buff_xml.append("<Amnt></Amnt>");
            buff_xml.append("<Prem></Prem>");
            buff_xml.append("<Mult></Mult>");
            buff_xml.append("<Copys>1</Copys>");
            buff_xml.append("<PayEndYearFlag></PayEndYearFlag>");
            buff_xml.append("<PayEndYear></PayEndYear>");
            buff_xml.append("<InsuYearFlag>" + map.get("InsuYearFlag") + "</InsuYearFlag>");
            buff_xml.append("<InsuYear>" + map.get("scope") + "</InsuYear>");
            buff_xml.append("</ITEM>");
        }
        buff_xml.append("</GRPWRAPLIST>");

        buff_xml.append("<WRAPPARAMLIST>");

        List<Map<String, Object>> picc_product_info =
            jdbcTemplate.queryForList("SELECT FacValAmnt,FacValAmntType,productid,projectid,RiskWrapCode,RiskCode,FacValAmnt/realprem Rate,realprem FROM picc_teamproduct WHERE BatchNo  = ?",
                new Object[] {BatchNo});

        for (Map map2 : picc_product_info)
        {
            List r22 =
                jdbcTemplate.queryForList("SELECT acode FROM insproduct WHERE id= ?",
                    new Object[] {map2.get("productid")});

            buff_xml.append("<ITEM>");
            buff_xml.append("<RiskWrapCode>" + map2.get("RiskWrapCode") + "</RiskWrapCode>");
            buff_xml.append("<RiskCode>" + map2.get("RiskCode") + "</RiskCode>");
            buff_xml.append("<DutyCode>" + ((Map)r22.get(0)).get("acode") + "</DutyCode>");
            buff_xml.append("<FacValAmnt>"
                + (int)Math.floor(Float.parseFloat(new StringBuilder().append(map2.get("FacValAmnt")).toString()))
                + "</FacValAmnt>");
            buff_xml.append("<Prem>" + map2.get("realprem") + "</Prem>");
            buff_xml.append("<FacValAmntType>" + map2.get("FacValAmntType") + "</FacValAmntType>");
            buff_xml.append("</ITEM>");
        }

        buff_xml.append("</WRAPPARAMLIST>");

        buff_xml.append("</GRPCONTDATA>");

        System.out.println(buff_xml);

        String encode = buff_xml.toString();
        //        String s2 = "sendXml=" + URLEncoder.encode(URLEncoder.encode(encode, "UTF-8"), "UTF-8");
        //
        //        String res = S1.sendPost("http://124.127.37.56:9088/hk", s2);
        
        String res_1 = EJBAxis2MethodTest.Axis2MethodSend(encode);

        //        if ("9999".equals(res))
        //        {
        //            return "发送 POST 请求出现异常";
        //        }

        //        String res_1 = URLDecoder.decode(res, "UTF-8");

        System.out.println(res_1);

        Pattern p = Pattern.compile("<PrtNo>(.*)</PrtNo>");
        Matcher mr = p.matcher(res_1);
        String PrtNo = "";
        String GrpContNo = "";

        if (res_1.indexOf("<State>00</State>") >= 0)
        {
            while (mr.find())
            {
                PrtNo = mr.group(1);
            }
            p = Pattern.compile("<GrpContNo>(.*)</GrpContNo>");
            mr = p.matcher(res_1);
            while (mr.find())
            {
                GrpContNo = mr.group(1);
            }

            String downurl =
                "http://e-shop.picchealth.com/pdf/checkPDF?contno=" + Des3.encode("piccandthirdpart", GrpContNo);
            String downurl1 =
                "http://e-shop.picchealth.com/pdf/checkPDF?contno="
                    + Des3.encode("piccandthirdpart", new StringBuilder(String.valueOf(GrpContNo)).append("-h")
                        .toString());

            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,downurl = ? , PrtNo = ? ,GrpContNo= ?, LegalPersonIDNo = ? WHERE BatchNo = ?",
                new Object[] {Integer.valueOf(7), downurl, PrtNo, GrpContNo, downurl1, BatchNo});
            System.out.println("PrtNo: " + PrtNo);
            System.out.println("GrpContNo: " + GrpContNo);
            System.out.println("downurl: " + downurl);
            return "0000";
        }
        p = Pattern.compile("<ErrInfo>(.*)</ErrInfo>");
        mr = p.matcher(res_1);
        while (mr.find())
        {
            if (mr.group(1).length() > 0)
            {
                return "出单失败：" + mr.group(1);
            }
        }

        return "0000";
    }

    public static void main(String[] args)
    {
        String res_1 = "<GRPCONTDATA><BACKMSGHEAD><Item><BatchNo>BYW2017112413321047813</BatchNo><SendDate>2017-11-24</SendDate><SendTime>13:32:27</SendTime><SendOperator>HKXS</SendOperator><MsgType>WX_GRP06</MsgType><State>00</State><ErrCode></ErrCode><ErrInfo></ErrInfo></Item></BACKMSGHEAD><GRPCONTLIST><Item><PrtNo>1E000004513</PrtNo><GrpContNo>00112792000001</GrpContNo><ManageCom>86420000</ManageCom><SaleChnl>03</SaleChnl><AgentCom>PY015005101</AgentCom><AgentCode>6103000002</AgentCode><FirstTrialOperator>gzh</FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-11-24</ReceiveDate><CoInsuranceFlag>1</CoInsuranceFlag><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><CValiDate>2017-11-25</CValiDate><CInValiDate>2018-11-24</CInValiDate><PayIntv>0</PayIntv><PayMode>1</PayMode><HandlerDate>2017-11-24</HandlerDate><PeoplesInsu>5</PeoplesInsu><Remark></Remark><EjbAgent>测试中介公司</EjbAgent></Item></GRPCONTLIST><GRPWRAPLIST><Item><RiskWrapCode>ZZJ080</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt>0.0</Amnt><Prem>228.50</Prem><Mult></Mult><Copys>1</Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>M</InsuYearFlag><InsuYear>12</InsuYear></Item></GRPWRAPLIST><WRAPPARAMLIST><Item><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>187.0</Prem><DutyInfo /></Item><Item><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>10000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>38.5</Prem><DutyInfo /></Item><Item><RiskWrapCode>ZZJ080</RiskWrapCode><RiskCode>551101</RiskCode><DutyCode>685001</DutyCode><FacValAmnt>100000</FacValAmnt><FacValAmntType>0</FacValAmntType><Prem>3.0</Prem><DutyInfo /></Item></WRAPPARAMLIST></GRPCONTDATA>";
        
        Pattern p = Pattern.compile("<PrtNo>(.*)</PrtNo>");
        Matcher mr = p.matcher(res_1);
        String PrtNo = "";
        String GrpContNo = "";

        if (res_1.indexOf("<State>00</State>") >= 0)
        {
            while (mr.find())
            {
                PrtNo = mr.group(1);
            }
            p = Pattern.compile("<GrpContNo>(.*)</GrpContNo>");
            mr = p.matcher(res_1);
            while (mr.find())
            {
                GrpContNo = mr.group(1);
            }

            String downurl =
                "http://e-shop.picchealth.com/pdf/checkPDF?contno=" + Des3.encode("piccandthirdpart", GrpContNo);
            String downurl1 =
                "http://e-shop.picchealth.com/pdf/checkPDF?contno="
                    + Des3.encode("piccandthirdpart", new StringBuilder(String.valueOf(GrpContNo)).append("-h")
                        .toString());

//            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,downurl = ? , PrtNo = ? ,GrpContNo= ?, LegalPersonIDNo = ? WHERE BatchNo = ?",
//                new Object[] {Integer.valueOf(7), downurl, PrtNo, GrpContNo, downurl1, BatchNo});
            System.out.println("PrtNo: " + PrtNo);
            System.out.println("GrpContNo: " + GrpContNo);
            System.out.println("downurl: " + downurl);
            System.out.println(downurl1);
    }
    }
}