package com.gopay.utils.hnbx;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class Mapp {
    public static void main(String[] args) {

        String s = "{\"ebResHead\":{\"userCode\":\"00000000\",\"reqTime\":\"2018-01-31 08:49:12\",\"resTime\":\"2018-01-31 08:49:19\",\"reqType\":\"02\",\"orderId\":\"BYW2018013108341268915\"},\"ebResBody\":{\"dealCode\":\"000000\",\"dealDesc\":\"交易成功\",\"certiNo\":[\"8116327142018000016\"]}}";

        Map<String, Object> m = JSON.parseObject(s, Map.class);

        Map<String, Object> ebResBody = JSON.parseObject(m.get("ebResBody") + "", Map.class);

        if (ebResBody.get("certiNo") == null) {
            System.out.println(ebResBody.get("dealDesc"));
        } else {
            String certiNo = ebResBody.get("certiNo").toString();
            System.out.println(certiNo.replace("\"", "")
                    .replace("[", "")
                    .replace("]", ""));
        }

//
//        System.out.println((JSON.parseObject(m.get("ebResBody") + "", Map.class)
//                .get("certiNo") + "").replace("\"", "")
//                .replace("[", "")
//                .replace("]", ""));
    }


}
