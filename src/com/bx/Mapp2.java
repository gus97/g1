package com.bx;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Mapp2 {

    public static void main(String[] args) {

        Map map = new LinkedHashMap();

        Map m1 = new LinkedHashMap();

        Map m2 = new LinkedHashMap();

        m2.put("A1","01");
        m2.put("A2","02");

        Map m3 = new LinkedHashMap();

        m3.put("B1",300);
        m3.put("B2","04");

        List res = new ArrayList();

        res.add(m2);
        res.add(m3);


        m1.put("a1", 1);
        m1.put("a2", 2);
        m1.put("a3", 3);
        m1.put("a4", 4);
        m1.put("list",res);
        map.put("A",m1);

        String s = JSON.toJSONString(map);

        //System.out.println(s);


    }
}
