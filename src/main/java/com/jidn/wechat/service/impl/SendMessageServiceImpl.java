package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.speech.SpeechApi;
import com.jidn.common.baidu.translate.TransApi;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.*;
import com.jidn.web.model.News;
import com.jidn.wechat.message.resp.*;
import com.jidn.wechat.service.SendMessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;


/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/27 15:12
 * @Description:
 */
@Service("sendMessageService")
public class SendMessageServiceImpl implements SendMessageService {

    @Resource
    RedisService redisService;

    @Override
    public String sendMessageText(String content,String openid,String mpid) {
       TextMessage txtMsg=new TextMessage();
       try {

           txtMsg.setToUserName(openid);
           txtMsg.setFromUserName(mpid);
           txtMsg.setCreateTime(new Date().getTime());
           txtMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
           if(StringUtils.isEmpty(content)){
               txtMsg.setContent(GlobalConstants.getInterfaceUrl("msg_text_hello"));
           } else {
               txtMsg.setContent(content);
           }
       } catch (Exception e){
           e.printStackTrace();
       }
       return MessageUtil.textMessageToXml(txtMsg);
    }

    @Override
    public String sendMessageImage(String content,String openid,String mpid) {
        ImageMessage imgMsg = new ImageMessage();
        Image img = new Image();
        try{
            imgMsg.setToUserName(openid);
            imgMsg.setFromUserName(mpid);
            imgMsg.setCreateTime(new Date().getTime());
            imgMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
            String mediaid= getMediaIdByVoice(content);
            img.setMediaId(mediaid);
            imgMsg.setImage(img);
        } catch (Exception e){
            e.printStackTrace();
        }
      return  MessageUtil.imageMessageToXml(imgMsg);
    }

    @Override
    public String sendMessageNews(String content,String openid, String mpid) {
        NewsMessage newMsg=new NewsMessage();
        newMsg.setToUserName(openid);
        newMsg.setFromUserName(mpid);
        newMsg.setCreateTime(new Date().getTime());
        newMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        try {
            Article article = filterNewsByKeyWrold(content);
            List<Article> list=new ArrayList<Article>();
            list.add(article); //如果需要发送多个 就加多个 Article 就OK
            newMsg.setArticleCount(list.size());
            newMsg.setArticles(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageUtil.newsMessageToXml(newMsg);
    }

    @Override
    public String sendMessageTranslate(String content,String openid, String mpid) {
        TextMessage txtMsg=new TextMessage();
        try {
            TransApi api = new TransApi(GlobalConstants.getInterfaceUrl("baiduApi"), GlobalConstants.getInterfaceUrl("baiduSecurityKey"));
            String  content_result = api.getTransResult(content);
            char [] content_result_temp = content_result.toCharArray();
            content_result = "";
            for(int i = content_result_temp.length-5;;i--) {
                if(content_result_temp[i] == '"') {
                    break;
                }
                content_result = content_result_temp[i] + content_result;
            }
            txtMsg.setToUserName(openid);
            txtMsg.setFromUserName(mpid);
            txtMsg.setCreateTime(new Date().getTime());
            txtMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            txtMsg.setContent(content_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageUtil.textMessageToXml(txtMsg);
    }


    @Override
    public String sendMessageVoice(String content,String openid, String mpid) {
        VoiceMessage voiceMsg = new VoiceMessage();
        try{
            String mediaId = getMediaIdByVoice(content);
            if(StringUtils.isEmpty(mediaId)){
                mediaId = getRealTimeMediaId(WeChatConstants.XJBSB);
            }
            Voice v = new Voice();
            voiceMsg.setToUserName(openid);
            voiceMsg.setFromUserName(mpid);
            voiceMsg.setCreateTime(new Date().getTime());
            voiceMsg.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_VOICE);
            v.setMediaId(mediaId);
            voiceMsg.setVoice(v);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageUtil.voiceMessageToXml(voiceMsg);
    }

    @Override
    public String sendMessageMusic(String content,String openid, String mpid) {
        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setToUserName(openid);
        musicMessage.setFromUserName(mpid);
        musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
        musicMessage.setCreateTime(new Date().getTime());
        try{
            Music music = new Music();
            music.setThumbMediaId("");
            music.setTitle("");
            music.setDescription("");
            music.setMusicUrl("");
            music.setHQMusicUrl("");
            musicMessage.setMusic(music);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageUtil.musicMessageToXml(musicMessage);
    }

    public Article filterNewsByKeyWrold(String content){
        Article article = new Article();
        try{
            if(WeChatConstants.ENTERTAINMENT.equals(content)){//娱乐
                String NewEntertainment = redisService.hget(WeChatConstants.REDIS_KEY, WeChatConstants.REDIS_NEW_ENTERTAINMENT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.FINANCE.equals(content)){//财经
                String NewEntertainment = redisService.hget(WeChatConstants.REDIS_KEY, WeChatConstants.REDIS_NEW_FINANCE);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.MILITARY.equals(content)){//军事
                String NewEntertainment = redisService.hget(WeChatConstants.REDIS_KEY, WeChatConstants.REDIS_NEW_MILITARY);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.SPORT.equals(content)){//体育
                String NewEntertainment = redisService.hget(WeChatConstants.REDIS_KEY, WeChatConstants.REDIS_NEW_SPORT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.HOT_SPOT.equals(content)){//热点
                String NewEntertainment = redisService.hget(WeChatConstants.REDIS_KEY, WeChatConstants.REDIS_NEW_HOT_SPORT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else {//博客
                article.setTitle(GlobalConstants.getInterfaceUrl("blog_name"));
                article.setPicUrl(GlobalConstants.getInterfaceUrl("blog_img"));
                article.setDescription(GlobalConstants.getInterfaceUrl("blog_description")); //图文消息的描述
                article.setUrl(GlobalConstants.getInterfaceUrl("blog_url"));  //图文 url 链接
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return article;
    }


    public Article getRandomArticle(List<News> news){
        Random random = new Random();
        News n = news.get(random.nextInt(news.size()));
        Article article = new Article();
        article.setTitle(n.getNewTitle());
        article.setDescription(n.getNewTitle());
        article.setPicUrl(n.getNewImg());
        article.setUrl(n.getNewHref());
        return article;
    }

    public String getMediaIdByVoice(String content){
        String mediaId = redisService.hget(WeChatConstants.WECHAT_VOICE, content);
        return mediaId;
    }


    public String getRealTimeMediaId(String content){
        String mediaId = "";
        try{
            String url = SpeechApi.synthesis(content,GlobalConstants.getInterfaceUrl("baiduSpeechApi"),
                    GlobalConstants.getInterfaceUrl("baiduSpeechApiKey"),GlobalConstants.getInterfaceUrl("baiduSpeechApiSecretKey"));
            mediaId =  FileUtil.WeChatUpload(url,GlobalConstants.getInterfaceUrl("access_token"),"voice");
        }catch (Exception e){
            e.printStackTrace();
        }
        return mediaId;
    }

}
