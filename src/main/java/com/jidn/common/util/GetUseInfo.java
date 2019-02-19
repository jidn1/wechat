package com.jidn.common.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.util.GlobalConstants;
import com.jidn.common.util.HttpUtils;


/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/27 14:49
 * Description: 获取微信用户信息
 */
public class GetUseInfo {


    /**
     * @Description: 通过 openid 获取用户微信信息
     * @param @param openid
     * @param @return
     * @param @throws Exception
     * @author jidn
     * @date 2018/12/27 14:49
     */
    public static HashMap<String, String> Openid_userinfo(String openid)
            throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token",
                GlobalConstants.getProperties("access_token"));  //定时器中获取到的 token
        params.put("openid", openid);  //需要获取的用户的 openid
        params.put("lang", "zh_CN");
        String subscribers = HttpUtils.sendGet(
                GlobalConstants.getProperties("OpenidUserinfoUrl"), params);
        System.out.println(subscribers);
        params.clear();
        //这里返回参数只取了昵称、头像、和性别
        params.put("nickname",
                JSONObject.parseObject(subscribers).getString("nickname")); //昵称
        params.put("headimgurl",
                JSONObject.parseObject(subscribers).getString("headimgurl"));  //图像
        params.put("sex", JSONObject.parseObject(subscribers).getString("sex"));  //性别
        return params;
    }

}
