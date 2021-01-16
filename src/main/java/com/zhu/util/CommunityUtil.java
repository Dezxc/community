package com.zhu.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密  salt
    public static String md5(String key){
        //isBlank 是用来判断key是否为null，空字符串，空格
            if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 转换json字符串
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map) {
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map != null) {
            for(String key : map.keySet()) {
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code,null,null);
    }

    // 简单测试一下json
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("name","zhuxinchen");
        map.put("age",25);
        System.out.println(getJSONString(200,"正确",map));
    }

}
