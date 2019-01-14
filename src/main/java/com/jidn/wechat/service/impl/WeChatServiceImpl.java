package com.jidn.wechat.service.impl;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.WeChatConstants;
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
    RedisService redisService;

    @Override
    public String switchModeLanguage(String content, String openid, String mpid) {
        try{
            String Language = content.substring(3);
            String toLan = "";
            if(WeChatConstants.Language_jp.equals(Language)){
                toLan = "jp";
            } else if(WeChatConstants.Language_kor.equals(Language)){
                toLan = "kor";
            } else if(WeChatConstants.Language_fra.equals(Language)){
                toLan = "fra";
            } else if(WeChatConstants.Language_ru.equals(Language)){
                toLan = "ru";
            } else if(WeChatConstants.Language_de.equals(Language)){
                toLan = "de";
            } else if(WeChatConstants.Language_en.equals(Language)){
                toLan = "en";
            }
            redisService.save(WeChatConstants.defaultLanguage,toLan,60*60);//切换语言默认时间为1小时
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
