package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:36
 * @Description: 语音消息
 */
@Data
public class VoiceMessage extends BaseMessage{
    private Voice Voice;

}
