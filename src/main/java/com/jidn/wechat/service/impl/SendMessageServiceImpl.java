package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.translate.TransApi;
import com.jidn.common.oss.OssUtil;
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
               txtMsg.setContent(GlobalConstants.getProperties("msg_text_hello"));
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
            Article article = filterNewsByKeyWorld(content);
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
            TransApi api = new TransApi(GlobalConstants.getProperties("baiduTranApi"), GlobalConstants.getProperties("baiduTranSecurityKey"));
            String  content_result = api.getTransResult(content,openid);
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
                mediaId = getRealTimeMediaId(content);
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

    @Override
    public String sendMessageTv(String content, String openid, String mpid) {
        TextMessage txtMsg=new TextMessage();
        try {
            String tvHref = redisService.hget(WeChatConstants.WECHAT_TV,content);
            txtMsg.setToUserName(openid);
            txtMsg.setFromUserName(mpid);
            txtMsg.setCreateTime(new Date().getTime());
            txtMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            System.out.println("===================="+tvHref);
            txtMsg.setContent("<a href=\""+tvHref+"\">"+content+"</a>");
        } catch (Exception e){
            e.printStackTrace();
        }
        return MessageUtil.textMessageToXml(txtMsg);
    }

    public Article filterNewsByKeyWorld(String content){
        Article article = new Article();
        try{
            if(WeChatConstants.ENTERTAINMENT.equals(content)){//娱乐
                String NewEntertainment = redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_ENTERTAINMENT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.FINANCE.equals(content)){//财经
                String NewEntertainment = redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_FINANCE);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.MILITARY.equals(content)){//军事
                String NewEntertainment = redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_MILITARY);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.SPORT.equals(content)){//体育
                String NewEntertainment = redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_SPORT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.HOT_SPOT.equals(content)){//热点
                String NewEntertainment = redisService.hget(WeChatConstants.WECHAT_NEWS, WeChatConstants.REDIS_NEW_HOT_SPORT);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else {//博客
                article.setTitle(GlobalConstants.getProperties("blog_name"));
                article.setPicUrl(GlobalConstants.getProperties("blog_img"));
                article.setDescription(GlobalConstants.getProperties("blog_description")); //图文消息的描述
                article.setUrl(GlobalConstants.getProperties("blog_url"));  //图文 url 链接
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
        content = content.replace(",","").replace("，","").replace("。","");
        String mediaId = redisService.hget(WeChatConstants.WECHAT_VOICE, content);
        if(!StringUtils.isEmpty(mediaId)){
            JSONArray array = JSONObject.parseArray(mediaId);
            Object randomOne = ListUtil.getRandomOne(array);
            return randomOne.toString();
        }
        return null;
    }

    public String getRealTimeMediaId(String content){
        String mediaId = "";
        List<String> mediaList = new ArrayList<String>();
        String urlFileName = "";
        String fileContent = content.replace(",","").replace("，","").replace("。","");
        try{
            urlFileName = redisService.hget(WeChatConstants.WECHAT_VOICE_FILE_PATH, fileContent);
            if(!StringUtils.isEmpty(urlFileName)){
                JSONArray array = JSONObject.parseArray(urlFileName);
                array.forEach(urlFile ->{
                    try {
                        String ossUrl = OssUtil.getUrl(urlFile.toString(), true);
                        String localUrl = FileUtil.uploadOssVideo(ossUrl, urlFile.toString());
                        String media = FileUtil.WeChatUpload(localUrl,"voice");
                        mediaList.add(media);
                        FileUtil.delete(localUrl);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });

                mediaId = ListUtil.getRandomOne(mediaList);
                redisService.hset(WeChatConstants.WECHAT_VOICE, content,JSONObject.toJSONString(mediaList));
            } else {
                redisService.hset(WeChatConstants.WECHAT_NO_REPLY,fileContent,WeChatConstants.XJMX);
                mediaId = redisService.hget(WeChatConstants.WECHAT_VOICE,WeChatConstants.XJMX);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mediaId;
    }

}
