package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:32
 * @Description: 文本消息消息体
 */
@Data
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;

}
