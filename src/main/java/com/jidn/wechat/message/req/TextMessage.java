package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:24
 * @Description:文本消息
 */
@Data
public class TextMessage extends BaseMessage{

    // 消息内容
    private String Content;

}
