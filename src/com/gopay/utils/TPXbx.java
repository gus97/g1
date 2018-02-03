package com.gopay.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.jdbc.core.JdbcTemplate;

public class TPXbx
{

    @SuppressWarnings("resource")
    public static String tpy(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid)
    {

        if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 12 AND kind=1") == 0)
        {
            return "AgentCom error 12 1";
        }

        if (jdbcTemplate.queryForInt("SELECT count(1) FROM toinscompany WHERE insid = 12 AND kind=4") == 0)
        {
            return "AgentCom4 error 12 4";
        }

        Map<String, Object> m1 =
            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND  insid = 12 AND kind=1",
                new Object[] {medid});

        Map<String, Object> m2 =
            jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany,medcompany  WHERE medcompany.id = ? AND medcompany.code = toinscompany.syscode AND   insid = 12 AND kind=4",
                new Object[] {medid});

        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final StringBuffer buff_xml = new StringBuffer();

        String tmp1 = "";

        buff_xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        tmp1 += "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        buff_xml.append("<document>");
        tmp1 += "<document>";
        buff_xml.append("<signature></signature>");
        tmp1 += "<signature></signature>";
        buff_xml.append("<request>");
        tmp1 += "<request>";

        buff_xml.append("<head>");
        buff_xml.append("<orderNo>" + BatchNo + "</orderNo>");
        buff_xml.append("<function>team</function>");
        buff_xml.append("<agencyCode>" + m1.get("agentcode") + "</agencyCode>");
        buff_xml.append("<productCode>" + m2.get("agentcode") + "</productCode>");
        buff_xml.append("<businessType>proposal</businessType>");
        buff_xml.append("<transTime>" + s.format(d) + "</transTime>");
        buff_xml.append("</head>");

        // ==================================================================================
        List<Map<String, Object>> picc_teamperson_res =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
                new Object[] {BatchNo});
        if (picc_teamperson_res == null || picc_teamperson_res.size() == 0)
        {
            return "err:投保人信息为空";
        }

        String idt = picc_teamperson_res.get(0).get("IDType").toString();
        //      组织机构代码证，71,1
        //      税务登记证，72,2
        //      营业执照，70,3
        //      社会信用码，95,4
        if ("71".equals(idt))
        {
            idt = "1";
        }
        else if ("72".equals(idt))
        {
            idt = "2";
        }
        else if ("70".equals(idt))
        {
            idt = "3";
        }
        else if ("95".equals(idt))
        {
            idt = "4";
        }
        else
        {
            idt = "99";
        }

        buff_xml.append("<body>");
        buff_xml.append("<applicant>");
        buff_xml.append("<appliType>2</appliType>");
        buff_xml.append("<appliName>" + picc_teamperson_res.get(0).get("InsuredName") + "</appliName>");
        buff_xml.append("<identifyType>" + idt + "</identifyType>");
        buff_xml.append("<identifyNumber>" + picc_teamperson_res.get(0).get("IDNo") + "</identifyNumber>");
        buff_xml.append("<appliAddress>" + picc_teamperson_res.get(0).get("BankAccNo") + "</appliAddress>");
        buff_xml.append("<contactName>" + picc_teamperson_res.get(0).get("BankCode") + "</contactName>");
        buff_xml.append("<contactPhone>" + picc_teamperson_res.get(0).get("Phone") + "</contactPhone>");
        buff_xml.append("</applicant>");
        
        // =========================================================================================
        List<Map<String, Object>> teamorderList =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamorder WHERE BatchNo = ?", new Object[] {BatchNo});

        //        int sumInsured =
        //            jdbcTemplate.queryForInt("SELECT Amnt sumInsured FROM Picc_teamorder where BatchNo = ?",
        //                new Object[] {BatchNo});
        //        int sumPremium =
        //            jdbcTemplate.queryForInt("SELECT realprem  sumPremium FROM Picc_teamorder where BatchNo = ?",
        //                new Object[] {BatchNo});
        buff_xml.append("<commonProject>");
        buff_xml.append("<geographicalArea>00001</geographicalArea>");
        buff_xml.append("<operateDate>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10)
            + " 00:00:00</operateDate>");
        buff_xml.append("<startDate>" + teamorderList.get(0).get("CValiDate").toString().substring(0, 10)
            + " 00:00:00</startDate>");
        buff_xml.append("<endDate>" + teamorderList.get(0).get("CInValiDate").toString().substring(0, 10)
            + " 00:00:00</endDate>");
        buff_xml.append("<currency>CNY</currency>");
        //        buff_xml.append("<sumInsured>" + sumInsured + "</sumInsured>");
        //        buff_xml.append("<sumPremium>" + sumPremium + "</sumPremium>");
        buff_xml.append("<sumInsured>" + teamorderList.get(0).get("Amnt") + "</sumInsured>");
        buff_xml.append("<sumPremium>" + teamorderList.get(0).get("realprem") + "</sumPremium>");
        buff_xml.append("</commonProject>");

        // =========================================================================================================

        //求人数
        //      SELECT COUNT(1) FROM picc_teamperson WHERE BatchNo='BYW2017111011595032663' AND personid IN (1,2)
        //求保额、保费
        //          SELECT SUM(realprem)*2,SUM(FacValAmnt)*2 FROM picc_teamproduct  WHERE BatchNo='BYW2017111011595032663' AND personid IN (1,2)
        
        for (int i = 0; i < 3; i++)
        {
            
            int n =
                jdbcTemplate.queryForInt("SELECT COUNT(1) FROM picc_teamperson WHERE BatchNo=? AND personid IN (?,?)",
                    new Object[] {BatchNo, i * 2 + 1, i * 2 + 2});
            
            if (n == 0)
                continue;
            
            List<Map<String, Object>> t2 =
                jdbcTemplate.queryForList("SELECT SUM(realprem)*? s1 ,SUM(FacValAmnt)*? s2 FROM picc_teamproduct  WHERE BatchNo=? AND personid IN (?,?)",
                    new Object[] {n, n, BatchNo, i * 2 + 1, i * 2 + 2});
            
            //            List<Map<String, Object>> info =
            //                jdbcTemplate.queryForList("SELECT interuser,interpwd,code,name FROM insproject WHERE id = ? ",
            //                    new Object[] {projectid});
            buff_xml.append("<projectDetail>");
            String s100 = "";
            String s99 = "";
            if (i == 0)
            {
                s99 = "A";
                s100 = "保险计划A";
            }
            else if (i == 1)
            {
                s99 = "B";
                s100 = "保险计划B";
            }
            else
            {
                s99 = "C";
                s100 = "保险计划c";
            }
            
            buff_xml.append("<projectCode>" + s99 + "</projectCode>"); //A
            
            buff_xml.append("<projectName>" + s100 + "</projectName>"); //保险计划A
            
            //            int peoplesInsu =
            //                jdbcTemplate.queryForInt("SELECT PeoplesInsu FROM Picc_teamorder where BatchNo = ?",
            //                    new Object[] {BatchNo});
            buff_xml.append("<sumInsured>" + t2.get(0).get("s2") + "</sumInsured>");
            buff_xml.append("<sumPremium>" + t2.get(0).get("s1") + "</sumPremium>");
            buff_xml.append("<uwCount>1</uwCount>");
            
            buff_xml.append("<quantity>" + n + "</quantity>");
            
            List<Map<String, Object>> picc_product_info =
                jdbcTemplate.queryForList("SELECT FacValAmnt,FacValAmntType,productid,projectid,RiskWrapCode,RiskCode,FacValAmnt/realprem Rate,realprem,Prem FROM picc_teamproduct WHERE BatchNo  = ?  AND personid IN (?,?)",
                    new Object[] {BatchNo, i * 2 + 1, i * 2 + 2});
            
            for (Map<String, Object> map : picc_product_info)
            {
                buff_xml.append("<itemKind>");
                buff_xml.append("<kindCode>" + map.get("RiskCode") + "</kindCode>");
                String name =
                    jdbcTemplate.queryForMap("select name from Insproduct where code = ?",
                        new Object[] {map.get("RiskCode")})
                        .get("name")
                        .toString();
                buff_xml.append("<kindName>" + name + "</kindName>");
                buff_xml.append("<unitInsured>" + map.get("FacValAmnt") + "</unitInsured>");
                buff_xml.append("<unitPremium>" + map.get("realprem") + "</unitPremium>");
                //          <deductibleDesc>免赔描述</deductibleDesc>
                //          kindCode='1119002' <limitDesc>每人每次事故绝对免赔额0元，超过合理医疗费用0元以上部分按100%比例赔付</limitDesc> else <limitDesc>限额描述</limitDesc>
                buff_xml.append("<deductibleDesc>免赔描述</deductibleDesc>");
                if ("1119002".equals(map.get("RiskCode").toString()))
                {
                    buff_xml.append("<limitDesc>每人每次事故绝对免赔额0元，超过合理医疗费用0元以上部分按100%比例赔付</limitDesc>");
                }
                else
                {
                    buff_xml.append("<limitDesc>限额描述</limitDesc>");
                }
                
                buff_xml.append("</itemKind>");
            }
            
            buff_xml.append("</projectDetail>");
            
        }

        // buff_xml.append("<currency>CNY</currency>");

        // ===========================================================================================================

        // ===============================================================================================================
        List<Map<String, Object>> picc_teamperson_bb =
            jdbcTemplate.queryForList("SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'",
                new Object[] {BatchNo});
        buff_xml.append("<insureds>");
        for (Map<String, Object> map : picc_teamperson_bb)
        {
            buff_xml.append("<insured>");
            //          <occupationCode>0101001</occupationCode>，取字段PiccOccupationCode
            buff_xml.append("<occupationCode>" + map.get("PiccOccupationCode") + "</occupationCode>");
            buff_xml.append("<insuredType>1</insuredType>");
            //          根据personid来取值，1、2=A

            String s101 = "";
            if ("1".equals(map.get("personid").toString()) || "2".equals(map.get("personid").toString()))
            {
                s101 = "A";
            }
            else if ("3".equals(map.get("personid").toString()) || "4".equals(map.get("personid").toString()))
            {
                s101 = "B";
            }
            else
            {
                s101 = "C";
            }

            buff_xml.append("<projectCode>" + s101 + "</projectCode>");
            buff_xml.append("<insuredName>" + map.get("insuredName") + "</insuredName>");

            idt = map.get("IDType").toString();
            if ("0".equals(idt))
            {
                idt = "01";
            }
            else if ("1".equals(idt))
            {
                idt = "02";
            }
            else if ("2".equals(idt))
            {
                idt = "03";
            }
            else if ("0".equals(idt))
            {
                idt = "04";
            }
            else
            {
                idt = "99";
            }

            buff_xml.append("<identifyType>" + idt + "</identifyType>");
            buff_xml.append("<identifyNumber>" + map.get("IDNo") + "</identifyNumber>");
            buff_xml.append("<sex>" + (Integer.parseInt(map.get("sex").toString()) + 1) + "</sex>");
            String appliRelation = picc_teamperson_res.get(0).get("RelationToInsured").toString();
            if (appliRelation.equals("00"))
            {
                appliRelation = "01";
            }
            if (appliRelation.equals("26") || appliRelation.equals("27"))
            {
                appliRelation = "99";
            }
            if (appliRelation.equals("04"))
            {
                appliRelation = "51";
            }
            if (appliRelation.equals("05"))
            {
                appliRelation = "52";
            }
            else
            {
                appliRelation = "99";
            }
            String p = map.get("Phone") == null ? "13912345678" : map.get("Phone").toString();
            buff_xml.append("<appliRelation>" + appliRelation + "</appliRelation>");
            buff_xml.append("<birthDate>" + map.get("Birthday").toString().substring(0, 10) + "</birthDate>");
            buff_xml.append("<mobilephone>" + p + "</mobilephone>");
            buff_xml.append("<address></address>");
            buff_xml.append("<postCode></postCode>");
            buff_xml.append("<Email></Email>");
            buff_xml.append("<BMI></BMI>");
            buff_xml.append("</insured>");
        }
        buff_xml.append("</insureds>");

        buff_xml.append("<specialClauses>");
        buff_xml.append("<clauseCode>T010</clauseCode>");
        buff_xml.append("<clauseCName>约定A</clauseCName>");
        buff_xml.append("<clauseContext>1、具体的保险责任、范围与条件以本保险单的约定为准；本保险单未尽事宜，以本保险单所附条款的规定为准。</clauseContext>");
        buff_xml.append("</specialClauses>");
        
        buff_xml.append("</body>");
        String tmp2 = "";
        buff_xml.append("</request>");
        tmp2 += "</request>";
        buff_xml.append("</document>");
        tmp2 += "</document>";
        
        String sn = buff_xml.toString().replace(tmp1, "");
        sn = sn.replace(tmp2, "");
        
        //        String privateKey =
        //            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJd/tWJ7EIc8pvu580NuOwuaD2R6oVi/NLhSUCcSwXIOTusMLhsM9QNcoqBRKgCozDku8QjzmNqKEwrDkvLJjuBoHt+h0X+2bsmgsWO+FYexpJ/mvKmhfkIQ0YW9ofHeIHWji9KWHAAPHGCU3WYW4ORP/OV0OybG6ZNd1L9YDa7ZAgMBAAECgYBJMjrAyhTCQlurY7xU5/0/LcAiG924sykVpS90sWslYCRhDBF6oFgAt9EbBBv3FZcWScfLO2aur+djW/qzsw6EP2s1SCeelKZCTAm3mMSyHg+X14ZlrOcowfzdOc611g7DgycOvOnZ9cfc2RHKMS4DCAM2yw5ikZZ4hNmNqGf3wQJBAMpbjD1aCPM1RiE5pYgkVKj31VrDHyfup0LELIINECwRdwhcBEOtsdg2Kx0ZjbV9rehulJoBLCM3D0RcKVdgxZ8CQQC/qMTxGqjauo6aqkrush0mad2BQJTL/N3eLS1Ti7M3pgH0OqghE3DBjnuSjZ4lK2zQzZTorDi7S/uI9gsOV4iHAkEAm/sHVb9SCM622SOLYi7HB1vKHOUs3eYLDd8Tg8e4AJAR1eK/8r8vyD6w6wRohE51QCYE69UdhaOKZCI6R70zdwJAOozfZVykmvWFHSDK9XK6TwLmZVcKXerpCLe6chxqaqSvUiIdPMWIm8jgXVwgJPDINF2pkQ8T4bwLosKvi/kXvwJBALZMRMNsblPvuWpDAAWTVAC+yH3rGoNRQ61nzB5A0C8GAvhzlz8sKAjjr6D4Yd6Itg5XhOf9jEdRcJNDBcSYtes=";
        
        String privateKey =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMSxtRAQKiaKY5NZCOAls9cVmEOelAQFtM2AQz4uUEQq9HNmKjLxx1iqKAJhq2nRkYde6tlj9g3pJiUzySUV56LEuFX2zFRLYfaP8EfYyBWf3NlZ7jiGzbURqQWnVNAwam/1N2oAuJjIme+rLPmP30TgC54vwj/McsjE8sdpQX/rAgMBAAECgYEArgdRXRoAQUNWYOt4x+Fz4gyQQNvQuFJYKy6d0CloCTg8OtIc6iAzsYYzig/ieujaKvzXK2qfLKWpg2bzRCXCYBqhm7m/uwK+Ls15Yx1cR9d38QLb6sG9gQAstmB9N86JzIUveOTaN4EhQfAPbJvZkRgidOGdnPCuFYuVcHEXN5ECQQDtC2rDJQ2Ba5lRh92QcgbBq1Rbixtp7XYH27cOOV5va8nIrSBPyyoVFlcBSCz7czkrCfJptgQA/XoUgFQvX0vTAkEA1GxE8CtMIP9eSGXlY8TRrrY2ca3Y1lwGRcDvFsIO6vXeF/d0oj4vNoK4zY923fTY3tKGGcU66jZqZzv5Xx7kiQJBAJaYfkdkLuReG5W0n4q5Jpm742gVRGFTv3zWlcGvqNNYw2V+t9x50k57VuM1m35UgalaNGv8eya14u1LajB+dokCQQCIWAoToabWkzKgA48t73mqy1e8tZo1VJHOGd0cXWiX1UI9Xd6l+IXzVgpqHohsRKkitDvQtLBw/oZjRrJ0JG8JAkAbpV/yTPhWehbWbOwQXhqzd6qmFN6BcGh7HG3RLRZ8S098XDEKbQ0pFVZ0Cfua/fKylDatiWCphdI5RcOxTsb6";

        String algorithm = "SHA256WITHRSA";

        String dataEncode;

        try
        {
            Base64 b64 = new Base64();

            dataEncode = b64.encodeToString(sn.toString().getBytes("UTF-8"));

            byte[] signBytes = SignUtil.sign(dataEncode.getBytes(), b64.decode(privateKey), algorithm);
            String sign = b64.encodeToString(signBytes);

            //            String httpsUrl = "https://car.etaiping.com:6016/react/portal/proposal";
            String httpsUrl = "https://nonautocloud.tpi.cntaiping.com/react/platform/portal/proposal";

            String sb = buff_xml.toString();

            sb = sb.replace("<signature></signature>", "<signature>" + sign + "</signature>");

            HttpPost httpPost = new HttpPost(httpsUrl);

            System.out.println(sb);

            HttpEntity requestEntity = new StringEntity(sb, "UTF-8");

            httpPost.setEntity(requestEntity);

            HttpClient httpClient = new SSLClient();

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity responsetEntity = httpResponse.getEntity();
            InputStream inputStream = responsetEntity.getContent();

            StringBuilder reponseXml = new StringBuilder();
            byte[] b = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(b)) != -1)
            {
                reponseXml.append(new String(b, 0, length));
            }

            System.out.println(reponseXml + "----");

            Pattern p = Pattern.compile("<policyNo>(.*)</policyNo>");
            Matcher mt = p.matcher(reponseXml);

            String PolicyNo = "";
            while (mt.find())
            {
                PolicyNo = mt.group(1);
            }

            p = Pattern.compile("<policyUrl>(.*)</policyUrl>");
            mt = p.matcher(reponseXml);

            String down = "";
            while (mt.find())
            {
                down = mt.group(1);
            }
            down = down.replace("<![CDATA[", "").replace("]]>", "");
            //                    .replace("insuredName", "appliName")
            //                    .replace("person", "group");

            System.out.println(down + "================" + PolicyNo);

            if (reponseXml.indexOf("<errorCode>0000</errorCode>") >= 0 && PolicyNo.length() > 0)
            {

                jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,downurl = ? , PrtNo = ? ,GrpContNo= ? WHERE BatchNo = ?",
                    new Object[] {7, down, "", PolicyNo, BatchNo});
                return "0000";
            }
            else
            {
                jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
                    new Object[] {6, reponseXml, BatchNo});
                return "出单失败：" + reponseXml;
            }

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "0000";

    }

    public static void main(String[] args)
    {

        String reponseXml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?><document><signature>QuuSEb3JWGZ4L0W8dJA7C6dfQDklA+g/PFge5xEXMKgLHB6UK9BzEHLFwZuOL2Ajp0US6SD/AFaWBiWNvl15+Qedy3Q6yCuvhn9gWBR39t+I3bZNUNvimFGLgY5Xg2nQkm6zdfw39RxZNsEuQfxjNBzKzgny30M2Aug58oWI3Vo=</signature><response><head><orderNo>BYW2017090116595076479</orderNo><function>team</function><agencyCode>HUAKAI</agencyCode><productCode>0200004</productCode><businessType>proposal</businessType><transTime>2017-09-01 17:00:04</transTime></head><body><successInd>1</successInd><errorCode>0000</errorCode><errorMessage></errorMessage><checkDataInd>1</checkDataInd><proposalNo>ET201700000000075572</proposalNo><policyNo>EP201700000000002979</policyNo><policyUrl><![CDATA[http://ectp.tpi.cntaiping.com/TPEBizWeb/pages/B2C/show/downLoad.do?policyNo=B9E13CF6F31D6EF22BB3285A809648AE152F378F85237BAB152F378F85237BABCDD1E859E192CAC22a1c4ef1h3hf6g3e&appliName=5FE2E637D134772A2a1c4ef1h3hf6g3e&queryConditionType=group]]></policyUrl></body></response></document>";

        Pattern p = Pattern.compile("<policyNo>(.*)</policyNo>");
        Matcher mt = p.matcher(reponseXml);

        String PolicyNo = "";
        while (mt.find())
        {
            PolicyNo = mt.group(1);
        }

        p = Pattern.compile("<policyUrl>(.*)</policyUrl>");
        mt = p.matcher(reponseXml);

        String down = "";
        while (mt.find())
        {
            down = mt.group(1);
        }
        down = down.replace("<![CDATA[", "").replace("]]>", "");

        System.out.println(down + "--------" + PolicyNo);

        System.out.println(reponseXml.indexOf("<errorCode>0000</errorCode>") >= 0 && PolicyNo.length() > 0);

    }
}
