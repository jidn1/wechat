package com.jidn.wechat.dispatcher;

import java.util.*;

import com.jidn.common.util.SpringUtil;
import com.jidn.wechat.controller.WechatSecurityController;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.common.util.MessageUtil;
import org.apache.log4j.Logger;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/27 10:55
 * @Description:事件消息业务分发器
 */
public class EventDispatcher {
    private static Logger logger = Logger.getLogger(WechatSecurityController.class);


    public static String processEvent(Map<String, String> map) {
        SendMessageService sendMessageService = (SendMessageService) SpringUtil.getBean("sendMessageService");
        String openid = map.get("FromUserName"); // 用户 openid
        String mpid = map.get("ToUserName"); // 公众号原始 ID

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { // 关注事件
            return sendMessageService.sendMessageText(null,openid,mpid);
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { //取消关注事件
            System.out.println("==============这是取消关注事件！");
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_LOCATION)) { //位置上报事件
            System.out.println("==============这是位置上报事件！");
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { //自定义菜单点击事件
            System.out.println("==============这是自定义菜单点击事件！");
        }

//        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_VIEW)) { //自定义菜单 View 事件
//            System.out.println("==============这是自定义菜单 View 事件！");
//        }

        return null;
    }
}
