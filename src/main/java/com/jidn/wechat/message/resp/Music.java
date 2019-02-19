package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:34
 * @Description: 音乐消息消息体
 */
@Data
public class Music {
    // 音乐名称
    private String Title;
    // 音乐描述
    private String Description;
    // 音乐链接
    private String MusicUrl;
    // 高质量音乐链接，WIFI 环境优先使用该链接播放音乐
    private String HQMusicUrl;

    private String ThumbMediaId; //缩略图的媒体 id

}
