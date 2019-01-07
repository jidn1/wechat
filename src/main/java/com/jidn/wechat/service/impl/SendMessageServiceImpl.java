package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.util.WeChatConstants;
import com.jidn.web.util.GlobalConstants;
import com.jidn.wechat.common.GetUseInfo;
import com.jidn.wechat.message.resp.*;
import com.jidn.wechat.service.SendMessageService;
import com.jidn.wechat.util.HttpPostUploadUtil;
import com.jidn.wechat.util.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/27 15:12
 * @Description:
 */
@Service("sendMessageService")
public class SendMessageServiceImpl implements SendMessageService {


    @Override
    public String sendMessageText(String openid,String mpid) {
       TextMessage txtMsg=new TextMessage();
       try {
           txtMsg.setToUserName(openid);
           txtMsg.setFromUserName(mpid);
           txtMsg.setCreateTime(new Date().getTime());
           txtMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
           txtMsg.setContent(GlobalConstants.getInterfaceUrl("msg_text_hello"));

       } catch (Exception e){
           e.printStackTrace();
       }
       return MessageUtil.textMessageToXml(txtMsg);
    }

    @Override
    public String sendMessageImage(String openid,String mpid) {
        ImageMessage imgMsg = new ImageMessage();
        Map<String, String> fileMap = new HashMap<String, String>();
        HttpPostUploadUtil util=new HttpPostUploadUtil();
        Image img = new Image();
        try{
            imgMsg.setToUserName(openid);
            imgMsg.setFromUserName(mpid);
            imgMsg.setCreateTime(new Date().getTime());
            imgMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);

            Map<String, String> textMap = new HashMap<String, String>();
            textMap.put("name", GlobalConstants.getInterfaceUrl("image_name"));
            fileMap.put("userfile", GlobalConstants.getInterfaceUrl("image_hello"));
            String mediaidrs = util.formUpload(textMap, fileMap);
            String mediaid= JSONObject.parseObject(mediaidrs).getString("media_id");
            img.setMediaId(mediaid);
            imgMsg.setImage(img);
        } catch (Exception e){
            e.printStackTrace();
        }
      return  MessageUtil.imageMessageToXml(imgMsg);
    }

    @Override
    public String sendMessageByContent(String content,String openid,String mpid) {
        ImageMessage imgMsg = new ImageMessage();
        TextMessage txtMsg=new TextMessage();
        Map<String, String> fileMap = new HashMap<String, String>();
        HttpPostUploadUtil util=new HttpPostUploadUtil();
        Image img = new Image();
        try{
            Map<String, String> imgMap = filterImageByKeyWrold(content);
            if(null == imgMap){
                txtMsg.setToUserName(openid);
                txtMsg.setFromUserName(mpid);
                txtMsg.setCreateTime(new Date().getTime());
                txtMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                txtMsg.setContent(GlobalConstants.getInterfaceUrl("msg_text_hello"));
                return MessageUtil.textMessageToXml(txtMsg);
            }

            imgMsg.setToUserName(openid);
            imgMsg.setFromUserName(mpid);
            imgMsg.setCreateTime(new Date().getTime());
            imgMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);

            Map<String, String> textMap = new HashMap<String, String>();
            textMap.put("name", imgMap.get("image_name"));
            fileMap.put("userfile", imgMap.get("image_url"));
            String mediaidrs = util.formUpload(textMap, fileMap);
            String mediaid= JSONObject.parseObject(mediaidrs).getString("media_id");
            img.setMediaId(mediaid);
            imgMsg.setImage(img);
        } catch (Exception e){
            e.printStackTrace();
        }
        return  MessageUtil.imageMessageToXml(imgMsg);
    }

    @Override
    public String sendMessageNews(String openid, String mpid) {
        NewsMessage newMsg=new NewsMessage();
        newMsg.setToUserName(openid);
        newMsg.setFromUserName(mpid);
        newMsg.setCreateTime(new Date().getTime());
        newMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        try {
            HashMap<String, String> userinfo= GetUseInfo.Openid_userinfo(openid);
            Article article=new Article();
            article.setDescription(GlobalConstants.getInterfaceUrl("msg_title")); //图文消息的描述
            article.setPicUrl(userinfo.get("headimgurl")); //图文消息图片地址
            article.setTitle("尊敬的："+userinfo.get("nickname")+",你好！\n"+GlobalConstants.getInterfaceUrl("msg_text_hello"));  //图文消息标题
            article.setUrl(GlobalConstants.getInterfaceUrl("blog_url"));  //图文 url 链接
            List<Article> list=new ArrayList<Article>();
            list.add(article); //如果需要发送多个 就加多个 Article 就OK
            newMsg.setArticleCount(list.size());
            newMsg.setArticles(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageUtil.newsMessageToXml(newMsg);
    }


    public Map<String,String> filterImageByKeyWrold(String content){
        Map<String,String> imgMap = new HashMap<String,String>();
        if(WeChatConstants.xjj.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_xjj");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        }else if(WeChatConstants.xzh.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_xzh");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        } else if(WeChatConstants.aj.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_hello");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        }else if(WeChatConstants.xubb.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_xbb");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        }else if(WeChatConstants.xiaok.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_xk");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        }else if(WeChatConstants.zhangy.equals(content)){
            String image_name = GlobalConstants.getInterfaceUrl("image_name");
            String image_url = GlobalConstants.getInterfaceUrl("image_zy");
            imgMap.put("image_name",image_name);
            imgMap.put("image_url",image_url);
        }
        return imgMap;
    }
}
