package com.jidn.common.baidu.translate;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/11 9:55
 * @Description:百度翻译API
 */
public class TransApi {

    private static final String TRANS_API_HOST = GlobalConstants.getProperties("translate_baidu");

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query,String openId) {
        Map<String, String> params = buildParams(query, "auto",getLanguage(openId));
        return HttpUtils.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from,String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }

    public String getLanguage(String openId){
        RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
        return redisService.get(WeChatConstants.DEFAULT_LANGUAGE+openId);
    }

}
