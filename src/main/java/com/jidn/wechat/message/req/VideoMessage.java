package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:26
 * @Description:视频/小视屏消息
 */
@Data
public class VideoMessage extends BaseMessage{

    private String MediaId; // 视频消息媒体 id，可以调用多媒体文件下载接口拉取数据
    private String ThumbMediaId; // 视频消息缩略图的媒体 id，可以调用多媒体文件下载接口拉取数据

}
