package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:24
 * @Description:地理位置消息
 */
@Data
public class LocationMessage extends BaseMessage{
    // 地理位置维度
    private String Location_X;
    // 地理位置经度
    private String Location_Y;
    // 地图缩放大小
    private String Scale;
    // 地理位置信息
    private String Label;


}
