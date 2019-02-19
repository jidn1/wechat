package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:32
 * @Description: 文本消息消息体
 */
@Data
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;

}
