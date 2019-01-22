package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:35
 * @Description: 视频消息
 */
@Data
public class VideoMessage extends BaseMessage{
    private Video Video;

}
