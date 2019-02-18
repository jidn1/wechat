package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.naturalLang.NaturlLangApi;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.GlobalConstants;
import com.jidn.common.util.WeChatConstants;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.wechat.service.WeChatService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
            redisService.save(WeChatConstants.DEFAULT_LANGUAGE+openid,toLan,60*5);//切换语言默认时间为5分钟
        }catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(WeChatConstants.SET_LANGUAGE_MSG.replace("LANG",Language),openid,mpid);
    }

    @Override
    public String sendMessageProcessing(String content, String openid, String mpid) {
        try{
            System.out.println("================================="+content);
            String newsContent = filterNewsByKeyWorld(content);
            String tvContent = redisService.hget(WeChatConstants.WECHAT_TV, content);
            if(!StringUtils.isEmpty(newsContent)){
              return sendMessageService.sendMessageNews(content,openid,mpid);
            } else if(!StringUtils.isEmpty(tvContent)){
                return sendMessageService.sendMessageTv(content,openid,mpid);
            } else if(!StringUtils.isEmpty(matchContent(content))){
                System.out.println("=================================进入语音合成");
                return sendMessageService.sendMessageVoice(content,openid,mpid);
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
        List<String> diaList = new ArrayList<String>();
        JSONArray array = new JSONArray();
        try {
            String[] dialogue = content.split(":")[1].split("=");
            String ifHas = redisService.hget(WeChatConstants.WECHAT_DIALOGUE, dialogue[0]);
            if(!StringUtils.isEmpty(ifHas)){
                array = JSONObject.parseArray(ifHas);
            }
            String[] moreDia = dialogue[1].split(";");
            for(String dia : moreDia){
                array.add(dia);
            }
            redisService.hset(WeChatConstants.WECHAT_DIALOGUE,dialogue[0],JSONObject.toJSONString(array));
            redisService.hset(WeChatConstants.WECHAT_DIALOGUE_TEM,dialogue[0],JSONObject.toJSONString(array));
        } catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(GlobalConstants.getProperties("msg_addDia"),openid,mpid);
    }

    @Override
    public String getRobotNoReply(String content, String openid, String mpid) {
        StringBuilder sbuilder = new StringBuilder("小吉同学未识别的问题如下：\n");
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
            switch (content){
                case WeChatConstants.ENTERTAINMENT:
                    return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_ENTERTAINMENT);
                case WeChatConstants.FINANCE:
                    return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_FINANCE);
                case WeChatConstants.MILITARY:
                    return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_MILITARY);
                case WeChatConstants.SPORT:
                    return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_SPORT);
                case WeChatConstants.HOT_SPOT:
                    return redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_HOT_SPORT);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String matchContent(String content){
        content = content.replace(",","").replace("，","").replace("。","");
        try {
            System.out.println("==============进入npl 自然语言匹配============");
            String mediaId = redisService.hget(WeChatConstants.WECHAT_VOICE, content);
            if(StringUtils.isEmpty(mediaId)){
                mediaId = redisService.hget(WeChatConstants.WECHAT_VOICE_FILE_PATH, content);
            }
            if(StringUtils.isEmpty(mediaId)) {
                Map<String, String> mediaIdMap = redisService.hgetAll(WeChatConstants.WECHAT_VOICE);
                Iterator<String> iter = mediaIdMap.keySet().iterator();
                System.out.println("==============进入npl 进入循环之前============");
                while (iter.hasNext()) {
                    String redisKey = iter.next();
                    BigDecimal SimilarRate = NaturlLangApi.SimilarTextNpl(redisKey, content);
                    if(SimilarRate.compareTo(WeChatConstants.SimilarRate) >= 0){
                        return redisKey;
                    }
                }
                System.out.println("==============进入npl 循环匹配结束============");

                Map<String, String> voicePathMap = redisService.hgetAll(WeChatConstants.WECHAT_VOICE_FILE_PATH);
                Iterator<String> voiIter = voicePathMap.keySet().iterator();
                System.out.println("==============进入npl 匹配oss路劲循环之前===========");
                while (voiIter.hasNext()) {
                    String redisKey = voiIter.next();
                    BigDecimal SimilarRate = NaturlLangApi.SimilarTextNpl(redisKey, content);
                    if(SimilarRate.compareTo(WeChatConstants.SimilarRate) >= 0){
                        return redisKey;
                    }
                }
                System.out.println("==============进入npl Oss匹配循环结束===========");
            } else {
                return content;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
