package com.jidn.wechat.service.impl;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.WeChatConstants;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.wechat.service.WeChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
                toLan = null;
            }
            redisService.save(WeChatConstants.DEFAULT_LANGUAGE,toLan,60*5);//切换语言默认时间为1小时
        }catch (Exception e){
            e.printStackTrace();
        }
        return sendMessageService.sendMessageText(WeChatConstants.SET_LANGUAGE_MSG.replace("LANG",Language),openid,mpid);
    }
}
