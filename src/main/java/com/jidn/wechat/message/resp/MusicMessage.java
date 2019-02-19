package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:35
 * @Description: 音乐消息
 */
@Data
public class MusicMessage extends BaseMessage {
    // 音乐
    private Music Music;

}
