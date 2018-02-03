package com.gopay.utils.hnbx;

import com.alibaba.fastjson.JSON;
import com.gopay.utils.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class HNBX {

    public static String hnbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) throws Exception {

        String req1 = requestHnbx(jdbcTemplate, BatchNo, companyid, projectid, medid, "01");

        Map m = JSON.parseObject(req1, Map.class);

        Map ebResBody = JSON.parseObject(m.get("ebResBody") + "", Map.class);

        String resp1 = ebResBody.get("dealCode").toString();

        if ("000000".equals(resp1)) {

            String req2 = requestHnbx(jdbcTemplate, BatchNo, companyid, projectid, medid, "02");

            Map m2 = JSON.parseObject(req2, Map.class);

            Map ebResBody2 = JSON.parseObject(m2.get("ebResBody") + "", Map.class);

            if (ebResBody.get("certiNo") == null) {
                System.out.println("req2==========>" + req2);
                return ebResBody2.get("dealDesc").toString();
            } else {
                String certiNo = ebResBody2.get("certiNo").toString().replace("\"", "")
                        .replace("[", "")
                        .replace("]", "");

                String down = "https://dev.icmp.chinahuanong.com.cn/ICMP/cp/Policy/download?orderNo=" + certiNo + "&imageType=UW0303";
                System.out.println("电子保单地址:\n" + down);

                jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? ,downurl = ? , PrtNo = ? ,GrpContNo= ? WHERE BatchNo = ?",
                        new Object[]{7, down, "", certiNo, BatchNo});

                return "0000";
            }


        } else {
            System.out.println("req1==========>" + req1);
            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ?, ErrInfo = ? WHERE batchno = ?",
                    new Object[]{6, ebResBody.get("dealDesc").toString(), BatchNo});
            return ebResBody.get("dealDesc").toString();
        }
    }

    public static String requestHnbx(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid, String type) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //最终请求报文
        Map<String, Object> fhnbw = new LinkedHashMap<>();

        //报文
        Map<String, Map<String, Object>> hnbw = new LinkedHashMap<>();

        //==========================================================
        Map<String, Object> ebReqPolicyHead = new LinkedHashMap<>();

        Map<String, Object> userCode = jdbcTemplate.queryForMap("SELECT agentcode FROM toinscompany where kind=1 and insid=13 limit 1");

        ebReqPolicyHead.put("userCode", userCode.get("agentcode") + "");
        ebReqPolicyHead.put("reqTime", format.format(new Date()));
        ebReqPolicyHead.put("reqType", type);
        ebReqPolicyHead.put("orderId", BatchNo);
        /**
         * 报文加入ebReqPolicyHead
         */
        hnbw.put("ebReqPolicyHead", ebReqPolicyHead);

        //===========================================================
        List<Map<String, Object>> picc_teamproduct =
                jdbcTemplate.queryForList("select acode,RiskCode,FacValAmnt,realprem,ROUND(realprem/FacValAmnt*1000,5) rate " +
                        " from insproduct, picc_teamproduct" +
                        " where RiskCode = code and insproduct.companyid = 13 and  " +
                        "picc_teamproduct.BatchNo = ? ", new Object[]{BatchNo});

        List<Map<String, Object>> code = jdbcTemplate.queryForList
                ("select code from insproject where id = ?", new Object[]{projectid});

        /**
         * 节点ebReqPolicyBody
         */
        Map<String, Object> ebReqPolicyBody = new LinkedHashMap<>();

        Map<String, Object> ebPolicyAgent = new LinkedHashMap<>();

        Map<String, Object> agentCode = jdbcTemplate.queryForMap("select agentcode from toinscompany where kind=2 and insid=13 limit 1");

        ebPolicyAgent.put("agentCode", agentCode.get("agentCode") + "");

        Map<String, Object> agreementCode = jdbcTemplate.queryForMap("select agentcode from toinscompany where kind=4 and insid=13 limit 1");

        ebPolicyAgent.put("agreementCode", agreementCode.get("agentcode") + "");


        ebReqPolicyBody.put("ebPolicyAgent", ebPolicyAgent);

        /**
         * 节点ebPolicyPlan，ebReqPolicyBody第一子节点
         */
        Map<String, Object> ebPolicyPlan = new LinkedHashMap<>();

        Map<String, Object> productCode = jdbcTemplate.queryForMap("select interuser from insproject where id = ? limit 1", new Object[]{projectid});

        ebPolicyPlan.put("productCode", productCode.get("interuser") + "");

        //ebPolicyPlan.put("productCode", "00002");

        ebPolicyPlan.put("planCode", code.get(0).get("code"));

        List<Map<String, Object>> teamorder =
                jdbcTemplate.queryForList
                        ("select * from picc_teamorder  where BatchNo = ? ", new Object[]{BatchNo});

        ebPolicyPlan.put("sumAmount", teamorder.get(0).get("Amnt"));
        ebPolicyPlan.put("insuredNum", teamorder.get(0).get("PeoplesInsu"));
        ebPolicyPlan.put("sumPremium", teamorder.get(0).get("realprem"));

        /**
         * 节点 ebPolicyItemKindsList
         */

        List<Map<String, Object>> p1 = jdbcTemplate.queryForList("select PiccOccupationCode,PiccOccupation " +
                "from picc_teamperson where RelationToInsured<>'00' and BatchNo= ? limit 1", new Object[]{BatchNo});


        List<Map<String, Object>> ebPolicyItemKindsList = new ArrayList<>();

        for (Map<String, Object> map : picc_teamproduct) {

            Map<String, Object> ebPolicyItemKinds = new LinkedHashMap<>();

            Map<String, Object> riskCode = jdbcTemplate.queryForMap("select agentcode from toinscompany where kind=3 and insid=13 and syscode='K001' limit 1");

            ebPolicyItemKinds.put("riskCode", riskCode.get("agentcode") + "");


            if (map.get("acode") != null) {
                ebPolicyItemKinds.put("itemCode", map.get("acode"));
            } else {
                ebPolicyItemKinds.put("itemCode", "");
            }

            /**
             * "modeCode": "010103",
             "coefficient": 1.1,
             "lostDays": 1,
             */


            ebPolicyItemKinds.put("kindCode", map.get("RiskCode"));

            ebPolicyItemKinds.put("modeCode", p1.get(0).get("PiccOccupationCode") + "");


            ebPolicyItemKinds.put("lostDays", 1);

            ebPolicyItemKinds.put("unitAmount", map.get("FacValAmnt"));
            ebPolicyItemKinds.put("quantity", teamorder.get(0).get("PeoplesInsu"));
            ebPolicyItemKinds.put("amount", Double.parseDouble(map.get("FacValAmnt") + "")
                    * Integer.parseInt(teamorder.get(0).get("PeoplesInsu") + ""));
            ebPolicyItemKinds.put("rate", map.get("rate"));
            ebPolicyItemKinds.put("premium", Double.parseDouble(map.get("realprem") + "")
                    * Integer.parseInt(teamorder.get(0).get("PeoplesInsu") + ""));

            ebPolicyItemKindsList.add(ebPolicyItemKinds);
        }

        /**
         * 插入ebPolicyItemKinds到ebPolicyPlan
         */
        ebPolicyPlan.put("ebPolicyItemKinds", ebPolicyItemKindsList);
        /**
         * 插入ebPolicyPlan到ebReqPolicyBody
         */
        ebReqPolicyBody.put("ebPolicyPlan", ebPolicyPlan);
        /**
         * 报文加入ebReqPolicyBody,其中报文子节点只有head和body，其它节点都属于body子节点
         */
        hnbw.put("ebReqPolicyBody", ebReqPolicyBody);

        /**
         * 节点ebPolicyPeriod，ebReqPolicyBody第二子节点
         */
        Map<String, Object> ebPolicyPeriod = new LinkedHashMap<>();

        ebPolicyPeriod.put("startDate", teamorder.get(0).get("CValiDate").toString().substring(0, 10));
        ebPolicyPeriod.put("startHour", 0);
        ebPolicyPeriod.put("endDate", teamorder.get(0).get("CInValiDate").toString().substring(0, 10));
        ebPolicyPeriod.put("endHour", 24);
        /**
         * 插入ebPolicyPeriod到ebReqPolicyBody
         */
        ebReqPolicyBody.put("ebPolicyPeriod", ebPolicyPeriod);


        Map<String, Object> ebPolicyHolder = new LinkedHashMap<>();
        ebPolicyHolder.put("policyHolderCname", teamorder.get(0).get("GrpName"));
        ebPolicyHolder.put("insuredType", "2");
        String OrgancomCode = (String) teamorder.get(0).get("OrgancomCode");
        if (OrgancomCode == null || OrgancomCode.equals("--")) {
            OrgancomCode = "99";
        } else {
            OrgancomCode = "09";
        }
        ebPolicyHolder.put("identifyType", OrgancomCode);
        String identifyNo = "";
        if (!"--".equals(teamorder.get(0).get("OrgancomCode") + "")) {
            identifyNo = (String) teamorder.get(0).get("OrgancomCode");
        } else if (!"--".equals(teamorder.get(0).get("TaxNo") + "")) {
            identifyNo = (String) teamorder.get(0).get("TaxNo");
        } else if (!"--".equals(teamorder.get(0).get("BusinessCode") + "")) {
            identifyNo = (String) teamorder.get(0).get("BusinessCode");
        } else if (!"--".equals(teamorder.get(0).get("CreditCode") + "")) {
            identifyNo = (String) teamorder.get(0).get("CreditCode");
        }
        ebPolicyHolder.put("identifyNo", identifyNo);
        ebPolicyHolder.put("countryFlag", "1");
        ebPolicyHolder.put("linkerName", teamorder.get(0).get("LinkMan1"));
        ebPolicyHolder.put("phoneNumber", teamorder.get(0).get("Phone1"));
        ebPolicyHolder.put("mobile", teamorder.get(0).get("Phone1"));
        if (teamorder.get(0).get("Phone1") != null) {
            ebPolicyHolder.put("email", teamorder.get(0).get("Phone1") + "@139.com");
        } else {
            ebPolicyHolder.put("email", "");
        }

        ebPolicyHolder.put("insuredNum", teamorder.get(0).get("PeoplesInsu"));

        //投保人


        //ebPolicyHolder.put("businessType", p1.get(0).get("PiccOccupationCode"));
        ebPolicyHolder.put("businessType", teamorder.get(0).get("personid") + "");

        ebPolicyHolder.put("occupationCode", p1.get(0).get("PiccOccupation"));

        /**
         * 插入ebPolicyHolder到ebReqPolicyBody
         */
        ebReqPolicyBody.put("ebPolicyHolder", ebPolicyHolder);


        //被保人
        List<Map<String, Object>> p2 = jdbcTemplate.queryForList("select InsuredName,IDType,IDNo,sex+1 sex," +
                "TIMESTAMPDIFF(year,Birthday,now())+1 age, Phone,PiccOccupationCode,InsuredID " +
                "from  picc_teamperson where BatchNo = ? and RelationToInsured <> '00'", new Object[]{BatchNo});
        /**
         * 节点 ebPolicyInsureds
         */
        List<Map<String, Object>> ebPolicyInsuredsList = new ArrayList<>();

        for (Map<String, Object> map : p2) {

            Map<String, Object> ebPolicyInsureds = new LinkedHashMap<>();

            ebPolicyInsureds.put("insuredCName", map.get("InsuredName"));
            ebPolicyInsureds.put("insuredType", "1");
            String IDType = map.get("IDType") + "";
            if ("0".equals(IDType)) {
                IDType = "01";
            } else if ("1".equals(IDType)) {
                IDType = "03";
            } else if ("5".equals(IDType)) {
                IDType = "02";
            } else if ("2".equals(IDType)) {
                IDType = "04";
            } else if ("3".equals(IDType)) {
                IDType = "05";
            } else {
                IDType = "99";
            }
            ebPolicyInsureds.put("identifyType", IDType);
            ebPolicyInsureds.put("identifyNo", map.get("IDNo"));
            ebPolicyInsureds.put("sex", map.get("sex") + "");
            ebPolicyInsureds.put("age", map.get("age"));
            ebPolicyInsureds.put("countryFlag", "0");

            if (map.get("Phone") != null) {
                ebPolicyInsureds.put("email", map.get("Phone") + "@139.com");
                ebPolicyInsureds.put("mobile", map.get("Phone"));
            } else {
                ebPolicyInsureds.put("mobile", "");
                ebPolicyInsureds.put("email", "");
            }
            //ebPolicyInsureds.put("occupationCode", map.get("PiccOccupationCode"));
            ebPolicyInsureds.put("occupationCode", "000101");
            ebPolicyInsureds.put("serino", map.get("InsuredID"));
            ebPolicyInsuredsList.add(ebPolicyInsureds);
        }

        /**
         * 插入ebPolicyInsureds到ebReqPolicyBody
         */
        ebReqPolicyBody.put("ebPolicyInsureds", ebPolicyInsuredsList);


        String bw = JSON.toJSONString(hnbw);

        //System.out.println(bw);

        String vmd5 = MD5Helper.encode(bw, "UTF-8");
        vmd5 = vmd5.toUpperCase();
        String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSIwNTKOzclWzW0H/oGpLG88dAGkpHbSoqFuR6+uvF5x5THsYBpTwik/viJPahiAWiOYFS0iVQEJamr7+wf3WhG1QIB+lCRoHdWUzly0icEUpRLVw5QH36IpdqH9WQ+GyuTJbt1ml4BeAKyB8A1iUnO1YHJtpAfU7wOnqiaHVs7AgMBAAECgYAL3Wi6b5LxmRoIt2KO7ye1QYDWlYLATeYEMzx0QSDOBRAlZHkiX3W5k9xnI850dAzR82Jy+Unl6x6kK6fziSC4JLjS4jSXwwbO0zfJFDViRBwijowN46LPw6f8MOk/Ao+F+gvxXaXfIz9rl8NibfSwjj/kg60G9NQRTa6SliWckQJBANCwiWemEmd00leQ257A+1J+xCO/T3ubjypcre0m2RE1ph7Ht5M30VKIiMcOymoBCeu9REErWEsM5JvSET9lLS8CQQDJ1Z+MYcll8QrUlUhFCY+3jCqUOLXa3omBngKhOqsJcQey4cjc5q2Onu+5aNyWmmwlX2BVN4w7P+ahENQzYee1AkBXJREL1j6jMxZO+K+tOHr6dPMk1lOzkz8Seocqm+lTFWscOVwAPvPLGja7oQKHh42EApIJALerB2RDaVJIfdCDAkEAqZcI77D+w9xsqEEjL3cgPi7VXCPmkhho2/OD7CtaqZ1Cxci3uP1PGcZsUZNNvnHUqvZgAFqjRumjhPSaCO6b/QJBAIMB7Q3CfoJUpApa+8l9+utdTM3+RxLrjo14c1PBjwV+zG1nuLKyJLOo+b+xSECgWCqYTWQQTamZ72xE6YHw8vw=";
        String sign = RSAHelper.encode(vmd5,
                RSAHelper.generatePrivate(priKey));

        fhnbw.put("sign", sign);
        fhnbw.put("message", bw);

        String fbw = JSON.toJSONString(fhnbw);

        //System.out.println(fbw);
        //System.out.println("\n\n");


        String httpsUrl = "https://dev.icmp.chinahuanong.com.cn/ICMP/cp/Insured/policy";

        HttpPost httpPost = new HttpPost(httpsUrl);

        HttpEntity requestEntity = new StringEntity(fbw, "UTF-8");

        httpPost.setEntity(requestEntity);

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        HttpClient httpClient = new SSLClient();

        HttpResponse httpResponse = httpClient.execute(httpPost);

        HttpEntity responsetEntity = httpResponse.getEntity();

        InputStream inputStream = responsetEntity.getContent();

        StringBuilder reponseJson = new StringBuilder();
        byte[] b = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(b)) != -1) {
            reponseJson.append(new String(b, 0, length));
        }

        return reponseJson.toString();
    }
}
