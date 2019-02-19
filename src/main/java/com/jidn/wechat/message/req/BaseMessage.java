package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:22
 * @Description:微信请求消息基本类
 */
@Data
public class BaseMessage {

    // 开发者微信号
    private String ToUserName;
    // 发送方帐号（一个 OpenID）
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/image/location/link/video/shortvideo）
    private String MsgType;
    // 消息 id，64 位整型
    private long MsgId;

}
