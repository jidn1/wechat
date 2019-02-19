package com.jidn.wechat.message.resp;

import lombok.Data;

import java.util.List;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/26 17:33
 * @Description: 多图文消息
 */
@Data
public class NewsMessage extends BaseMessage {

    // 图文消息个数，限制为 10 条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个 item 为大图
    private List<Article> Articles;

}
