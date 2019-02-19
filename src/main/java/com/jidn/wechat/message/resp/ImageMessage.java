package com.jidn.wechat.message.resp;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:34
 * Description:  图片消息
 */
@Data
public class ImageMessage extends  BaseMessage{
    private Image Image;

}
