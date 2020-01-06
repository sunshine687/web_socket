package com.wen.web_socket.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号码工具类
 */
@Component
public class MobileUtil {

    @Value("${apiUrl}")
    private String apiUrl;

    /**
     * 获取手机号码归属地
     * @param mobile //手机号码
     * @return //返回自定义手机号码信息
     */
    public String getMobileInfo(String mobile){
        HttpClient httpClient = new HttpClient();
        String mobileInfo = httpClient.doGet(apiUrl + "?tel=" + mobile);
        return mobileInfo;
    }

    /**
     * 封装json数据
     * @param mobile //手机号码
     * @param mobileInfo //手机号码信息
     * @return 返回自定义手机号码信息
     */
    private String formatObj(String mobile,String mobileInfo){
        Map mapObj = JSONObject.parseObject(mobileInfo,Map.class);
        //获取响应数据
        JSONObject responseJson = (JSONObject)mapObj.get("response");
        String response = JSONObject.toJSONString(responseJson);
        Map responseMap = JSONObject.parseObject(response,Map.class);
        //获取手机数据
        JSONObject mobileDataJson = (JSONObject)responseMap.get(mobile);
        String mobileData;
        mobileData = JSONObject.toJSONString(mobileDataJson);
        Map<String,Object> mobileDataMap = JSONObject.parseObject(mobileData, HashMap.class);
        mobileDataMap.put("mobile",mobile);
        return JSONObject.toJSONString(mobileDataMap);
    }
}
