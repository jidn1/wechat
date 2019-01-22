package com.jidn.wechat.message.req;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 17:23
 * @Description:连接消息
 */
@Data
public class LinkMessage extends BaseMessage{

    // 消息标题
    private String Title;
    // 消息描述
    private String Description;
    // 消息链接
    private String Url;

}
