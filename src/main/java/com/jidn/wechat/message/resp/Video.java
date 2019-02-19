package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:35
 * @Description: 视频消息体
 */
@Data
public class Video {
    private String MediaId;
    private String Title;
    private String Description;

}
