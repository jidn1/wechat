package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.GlobalConstants;
import com.jidn.common.util.WeChatConstants;
import com.jidn.web.model.News;
import com.jidn.wechat.message.resp.Article;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.wechat.service.WeChatService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/14 19:52
 * @Description:
 */
@Service("weChatService")
public class WeChatServiceImpl implements WeChatService {

    @Resource
    SendMessageService sendMessageService;

    @Resource
    RedisService redisService;

    @Override
    public String switchModeLanguage(String content, String openid, String mpid) {
        String Language = "";
        String toLan = "";
        try{
            Language = content.substring(3).replace(",","");
            if(WeChatConstants.LANGUAGE_JP.equals(Language)){
                toLan = "jp";
            } else if(WeChatConstants.LANGUAGE_KOR.equals(Language)){
                toLan = "kor";
            } else if(WeChatConstants.LANGUAGE_FRA.equals(Language)){
                toLan = "fra";
            } else if(WeChatConstants.LANGUAGE_RU.equals(Language)){
                toLan = "ru";
            } else if(WeChatConstants.LANGUAGE_DE.equals(Language)){
                toLan = "de";
            } else if(WeChatConstants.LANGUAGE_EN.equals(Language)){
                toLan = "en";
            } else if(WeChatConstants.LANGUAGE_ZH.equals(Language)){
                toLan = "";
            } else {
                toLan = "";
            }
            redisService.save(WeChatConstants.DEFAULT_LANGUAGE,toLan,60*5);//切换语言默认时间为5分钟
        }catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(WeChatConstants.SET_LANGUAGE_MSG.replace("LANG",Language),openid,mpid);
    }

    @Override
    public String sendMessageProcessing(String content, String openid, String mpid) {
        try{
            String newsContent = filterNewsByKeyWorld(content);
            String tvContent = redisService.hget(WeChatConstants.WECHAT_TV, content);
            if(!StringUtils.isEmpty(newsContent)){
              return sendMessageService.sendMessageNews(content,openid,mpid);
            } else if(!StringUtils.isEmpty(tvContent)){
                return sendMessageService.sendMessageTv(content,openid,mpid);
            } else {
                return sendMessageService.sendMessageText(GlobalConstants.getProperties("msg_text_error"),openid,mpid);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String addRobotDialogue(String content, String openid, String mpid) {
        try {
            String[] dialogue = content.split(":")[1].split("=");
            redisService.hset(WeChatConstants.WECHAT_DIALOGUE,dialogue[0],dialogue[1]);
            redisService.hset(WeChatConstants.WECHAT_DIALOGUE_TEM,dialogue[0],dialogue[1]);
        } catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(GlobalConstants.getProperties("msg_addDia"),openid,mpid);
    }

    @Override
    public String getRobotNoReply(String content, String openid, String mpid) {
        StringBuilder sbuilder = new StringBuilder("小吉同学识别的问题如下：\n");
        try {
            Map<String, String> replyMap = redisService.hgetAll(WeChatConstants.WECHAT_NO_REPLY);
            replyMap.forEach((k, v) -> {
                sbuilder.append(k+"\n");
            });
        } catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(sbuilder.toString(),openid,mpid);
    }

    public String filterNewsByKeyWorld(String content){
        try{
            if(WeChatConstants.ENTERTAINMENT.equals(content)){//娱乐
                return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_ENTERTAINMENT);
            } else if(WeChatConstants.FINANCE.equals(content)){//财经
                return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_FINANCE);
            } else if(WeChatConstants.MILITARY.equals(content)){//军事
                return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_MILITARY);
            } else if(WeChatConstants.SPORT.equals(content)){//体育
                return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_SPORT);
            } else if(WeChatConstants.HOT_SPOT.equals(content)){//热点
                return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_HOT_SPORT);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
