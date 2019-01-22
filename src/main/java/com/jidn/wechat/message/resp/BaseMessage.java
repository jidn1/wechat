package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:32
 * Description: 返回消息体-基本消息
 */
@Data
public class BaseMessage {

    // 接收方帐号（收到的 OpenID）
    private String ToUserName;
    // 开发者微信号
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/music/news）
    private String MsgType;

}
