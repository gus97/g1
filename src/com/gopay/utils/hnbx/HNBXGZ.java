package com.gopay.utils.hnbx;

import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class HNBXGZ {

    public static String hnbxgz(JdbcTemplate jdbcTemplate, String BatchNo, int companyid, int projectid, int medid) throws Exception {


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
        ebReqPolicyHead.put("reqType", "01");
        ebReqPolicyHead.put("orderId", BatchNo);

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

        List<Map<String, Object>> teamorder =
                jdbcTemplate.queryForList
                        ("select * from picc_teamorder  where BatchNo = ? ", new Object[]{BatchNo});

        List<Map<String,Object>> ebPolicyAddressesList = new ArrayList<>();
        Map<String,Object> ebPolicyAddressesMap = new LinkedHashMap<>();
        ebPolicyAddressesMap.put("serialNo","1");
        ebPolicyAddressesMap.put("address",teamorder.get(0).get("GrpAddress")+"");
        ebPolicyAddressesMap.put("postCode",teamorder.get(0).get("GrpZipCode")+"");
        ebPolicyAddressesList.add(ebPolicyAddressesMap);

        ebReqPolicyBody.put("ebPolicyAddresses",ebPolicyAddressesList);









        return "0000";
    }

}
