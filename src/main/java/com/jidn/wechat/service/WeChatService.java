package com.jidn.wechat.service;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/14 19:51
 * @Description: 微信功能接口
 */
public interface WeChatService {

    public String switchModeLanguage(String content,String openid,String mpid);

    public String sendMessageProcessing(String content,String openid,String mpid);
}
