package com.jidn.wechat.service;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/14 19:51
 * @Description: 微信功能接口
 */
public interface WeChatService {

    public String switchModeLanguage(String content,String openid,String mpid);

    public String sendMessageProcessing(String content,String openid,String mpid);

    public String addRobotDialogue(String content,String openid,String mpid);

    public String getRobotNoReply(String content,String openid,String mpid);
}
