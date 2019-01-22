package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:23
 * @Description:图片消息
 */
@Data
public class ImageMessage extends BaseMessage{

    // 图片链接
    private String PicUrl;

}
