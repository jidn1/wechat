package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:26
 * Description: 语音消息
 */
@Data
public class VoiceMessage extends BaseMessage {

    // 媒体 ID
    private String MediaId;
    // 语音格式
    private String Format;

}
