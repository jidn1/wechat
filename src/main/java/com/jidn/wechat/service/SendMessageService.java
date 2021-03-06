package com.jidn.wechat.service;

import com.jidn.wechat.message.resp.ImageMessage;
import com.jidn.wechat.message.resp.TextMessage;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/27 15:11
 * @Description:消息处理
 */
public interface SendMessageService {

    /**
     * 发送文本消息
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageText(String content,String openid,String mpid);

    /**
     * 发送图片消息
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageImage(String content,String openid,String mpid);

    /**
     * 发送图文消息
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageNews(String content,String openid,String mpid);

    /**
     * 文本翻译消息
     * @param content
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageTranslate(String content,String openid,String mpid);

    /**
     * 发送语音消息
     * @param content
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageVoice(String content,String openid,String mpid);

    /**
     * 发送音乐消息
     * @param content
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageMusic(String content,String openid,String mpid);

    /**
     * 发送链接消息
     * @param content
     * @param openid
     * @param mpid
     * @return
     */
    public String sendMessageTv(String content,String openid,String mpid);

  //  public String

}
