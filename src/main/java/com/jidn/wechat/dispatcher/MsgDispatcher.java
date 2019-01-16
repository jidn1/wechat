package com.jidn.wechat.dispatcher;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.SpringUtil;
import com.jidn.common.util.WeChatConstants;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.common.util.MessageUtil;
import com.jidn.wechat.service.WeChatService;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:27
 * @Description:消息业务处理分发器
 */
public class MsgDispatcher {



    public static String processMessage(Map<String, String> map) {
        SendMessageService sendMessageService = (SendMessageService)SpringUtil.getBean("sendMessageService");
        WeChatService weChatService = (WeChatService)SpringUtil.getBean("weChatService");
        RedisService redisService = (RedisService) SpringUtil.getBean("redisService");

        String defaultLan = redisService.get(WeChatConstants.DEFAULT_LANGUAGE);
        String openid=map.get("FromUserName"); //用户 openid
        String mpid=map.get("ToUserName");   //公众号原始 ID

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            String content=map.get("Content");
            String mmz=map.get("Content").replace(",","").replace("，","").replace("。","");

            if(WeChatConstants.MMZ.equals(mmz)){
                return sendMessageService.sendMessageImage(content,openid,mpid);
            } else {
                return sendMessageService.sendMessageVoice(content,openid,mpid);
            }

//            if(content.startsWith(WeChatConstants.switchMode)){
//                return weChatService.switchModeLanguage(content,openid,mpid);
//            } else {
//                if(StringUtils.isEmpty(defaultLan)){
//                    return sendMessageService.sendMessageNews(content,openid,mpid);
//                } else {
//                    return sendMessageService.sendMessageTranslate(content,openid,mpid);
//                }
//            }
        }

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
            return sendMessageService.sendMessageText(null,openid,mpid);
        }

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
            return sendMessageService.sendMessageText(null,openid,mpid);
        }

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { // 位置消息
           return sendMessageService.sendMessageText(null,openid,mpid);
        }

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) { // 视频消息
            return sendMessageService.sendMessageText(null,openid,mpid);
        }

        if (map.get(WeChatConstants.MSG_TYPE).equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { // 语音消息
            String recognition=map.get("Recognition");
            if(recognition.startsWith(WeChatConstants.SWITCH_MODE)){
                return weChatService.switchModeLanguage(recognition,openid,mpid);
            } else {
                if (StringUtils.isEmpty(defaultLan)) {

                } else {
                    return sendMessageService.sendMessageTranslate(recognition, openid, mpid);
                }
            }
        }

        return null;
    }
}
