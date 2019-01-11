package com.jidn.wechat.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.jidn.web.util.GlobalConstants;
import com.jidn.common.util.HttpUtils;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 18:04
 * Description: 微信两小时定时任务体
 */
public class WeChatTask {

    /**
     * @Description: 任务执行体
     * @param @throws Exception
     * @author jidn
     * @date 2018/12/26 18:04
     */
    public void getToken_getTicket() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credential");
        params.put("appid", GlobalConstants.getInterfaceUrl("appid"));
        params.put("secret", GlobalConstants.getInterfaceUrl("AppSecret"));
        String jstoken = HttpUtils.sendGet(
                GlobalConstants.getInterfaceUrl("tokenUrl"), params);
        String access_token = JSONObject.parseObject(jstoken).get("access_token").toString(); // 获取到 token 并赋值保存
        GlobalConstants.interfaceUrlProperties.put("access_token", access_token);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"token 为=============================="+access_token);
    }
}
