package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:34
 * Description:  图片消息
 */
@Data
public class ImageMessage extends  BaseMessage{
    private Image Image;

}
