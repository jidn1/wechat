package com.jidn.wechat.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.speech.SpeechApi;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.GlobalConstants;
import com.jidn.common.util.SpringUtil;
import com.jidn.common.util.WeChatConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/16 15:36
 * Description:
 */
public class RobotTask {

    public void Init_Dialogue(){
        try{
            RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
            Map<String, String> map = redisService.hgetAll(WeChatConstants.WECHAT_DIALOGUE_TEM);
            if(null != map){
                long start = System.currentTimeMillis();
                System.out.println("新增机器人对话开始==========================="+System.currentTimeMillis());
                map.forEach((k, v) -> {
                    System.out.println(k+"================"+v);
                    String ossUrl = SpeechApi.synthesis(v, GlobalConstants.getProperties("baiduSpeechApi"),
                            GlobalConstants.getProperties("baiduSpeechApiKey"), GlobalConstants.getProperties("baiduSpeechApiSecretKey"));
                    System.out.printf("==========================="+ossUrl);
                    redisService.hset(WeChatConstants.WECHAT_VOICE_FILE_PATH,k,ossUrl);
                });
                System.out.println("新增机器人对话结束耗时：==========================="+(System.currentTimeMillis()-start));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
