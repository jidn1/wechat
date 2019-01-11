package com.jidn.wechat.dispatcher;

import com.jidn.common.util.SpringUtil;
import com.jidn.common.util.WeChatConstants;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.common.util.MessageUtil;

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
        String openid=map.get("FromUserName"); //用户 openid
        String mpid=map.get("ToUserName");   //公众号原始 ID

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            String content=map.get("Content");
            System.out.println(content);
            if(content.startsWith(WeChatConstants.translate)) {
                return sendMessageService.sendMessageTranslate(content,openid,mpid);
            } else {
                return sendMessageService.sendMessageNews(content,openid,mpid);
            }
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
            return sendMessageService.sendMessageImage(openid,mpid);
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
            return sendMessageService.sendMessageText(openid,mpid);
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { // 位置消息
           return sendMessageService.sendMessageText(openid,mpid);
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) { // 视频消息
            return sendMessageService.sendMessageText(openid,mpid);
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { // 语音消息
            String recognition=map.get("Recognition");

            if(recognition.startsWith(WeChatConstants.translate)) {
                if(recognition.indexOf("语") == 3){
                    recognition = recognition.replaceFirst("语，","语:");
                } else if(recognition.indexOf("译") == 1){
                    recognition = recognition.replaceFirst("译，","译:");
                }
                System.out.println("========================语音消息"+recognition);
                return sendMessageService.sendMessageTranslate(recognition,openid,mpid);
            } else {
                return sendMessageService.sendMessageNews(recognition,openid,mpid);
            }
        }

        return null;
    }
}
