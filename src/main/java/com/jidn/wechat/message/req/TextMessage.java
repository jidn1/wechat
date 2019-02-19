package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:24
 * @Description:文本消息
 */
@Data
public class TextMessage extends BaseMessage{

    // 消息内容
    private String Content;

}
