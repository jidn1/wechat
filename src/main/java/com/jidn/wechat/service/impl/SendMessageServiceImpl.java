package com.jidn.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.translate.TransApi;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.*;
import com.jidn.web.model.News;
import com.jidn.wechat.message.resp.*;
import com.jidn.wechat.service.SendMessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.awt.windows.WEmbeddedFrame;

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
            String  content_result = api.getTransResult(content, content);
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
           // String mediaId = FileUtil.WeChatUpload("D:\\ideaWorkSpace\\gitProject\\wechat\\4ace38b7-8545-44d0-a0cb-9e341c0245d3.MP3", GlobalConstants.getInterfaceUrl("access_token"), "voice");
            Voice v = new Voice();
            voiceMsg.setToUserName(openid);
            voiceMsg.setFromUserName(mpid);
            voiceMsg.setCreateTime(new Date().getTime());
            voiceMsg.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_VOICE);
            v.setMediaId("vdvD0M__3-2y4HgCUL0HUBYbhWRSGMJUxXGoK_0K04wfAhUnKr1s4aExoMG3XT5_");
            voiceMsg.setVoice(v);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageUtil.voiceMessageToXml(voiceMsg);
    }

    @Override
    public String sendMessageMusic(String content,String openid, String mpid) {
        MusicMessage musicMessage = new MusicMessage();
        try{
            Music music = new Music();



            music.setThumbMediaId("4Ln9zjbEdBjMWS-TtYCmd89stx7CVmmmMGx1wdKul9-g6zOHQYJMukPEsC0pVd4h");

            music.setTitle("达康书记不同意");

            music.setDescription("达康书记不容易——山东工商学院杨军老师作词作曲演唱");

            music.setMusicUrl("http://wchat.nat123.cc/weichat/output.mp3");

            music.setHQMusicUrl("http://wchat.nat123.cc/weichat/output.mp3");


            musicMessage.setToUserName(openid);
            musicMessage.setFromUserName(mpid);

            musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);

            musicMessage.setCreateTime(new Date().getTime());

            musicMessage.setMusic(music);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageUtil.musicMessageToXml(musicMessage);
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

    public Article filterNewsByKeyWrold(String content){
        Article article = new Article();
        try{
            if(WeChatConstants.history.equals(content)) {
                article.setTitle("周总理遗言交待了什么绝密任务，空军飞行员含泪执行？");  //图文消息标题
                article.setPicUrl("https://p1.ifengimg.com/2019_02/1978F8F1393C57377F0984D81DC1C79987B25409_w800_h1116.jpg"); //图文消息图片地址
                article.setDescription("敬爱的周总理自1976年1月8日与世长辞，到今天已经43年了，他是为人民鞠躬尽瘁、死而后已的一代伟人，人民至今仍然非常景仰和缅怀他。我写下本文，以寄托对周总理的无限思念之情。"); //图文消息的描述
                article.setUrl("https://history.ifeng.com/c/7jIjYDJyAPQ");  //图文 url 链接
            } else if(WeChatConstants.read.equals(content)){
                article.setTitle("57岁周星驰自述青春：放下过往，也是放过自己");
                article.setPicUrl("http://e0.ifengimg.com/07/2019/0109/872F10B9D95544C4B19E2D5B6438B72AEB788F0C_size20_w478_h480.jpeg");
                article.setDescription("曾有人这样评价周星驰的电影：\n 小时候，看他的电影，哪怕哭得再凶，也能破涕为笑；\n 长大后，再看他的电影，往往笑着笑着，就哭了。"); //图文消息的描述
                article.setUrl("http://culture.ifeng.com/c/7jJJWkt8Jqe");  //图文 url 链接
            } else if(WeChatConstants.entertainment.equals(content)){//娱乐
                String NewEntertainment = redisService.hget(WeChatConstants.RedisKey, WeChatConstants.redisNewEntertainment);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.finance.equals(content)){//财经
                String NewEntertainment = redisService.hget(WeChatConstants.RedisKey, WeChatConstants.redisNewFinance);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.military.equals(content)){//军事
                String NewEntertainment = redisService.hget(WeChatConstants.RedisKey, WeChatConstants.redisNewMilitary);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.sport.equals(content)){//体育
                String NewEntertainment = redisService.hget(WeChatConstants.RedisKey, WeChatConstants.redisNewSport);
                List<News> news = JSONObject.parseArray(NewEntertainment, News.class);
                article = getRandomArticle(news);
            } else if(WeChatConstants.hotSpot.equals(content)){//热点
                String NewEntertainment = redisService.hget(WeChatConstants.RedisKey, WeChatConstants.redisNewHotSpot);
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

    public String ifSwitchLang(String content){
        StringBuilder sbLan = new StringBuilder(WeChatConstants.translate);
        try{
            String defaultLan = redisService.get(WeChatConstants.defaultLanguage);
            if(!StringUtils.isEmpty(defaultLan)){
                if("jp".equals(defaultLan)){
                    sbLan.append(WeChatConstants.Language_jp);
                } else if("kor".equals(defaultLan)){
                    sbLan.append(WeChatConstants.Language_kor);
                } else if("fra".equals(defaultLan)){
                    sbLan.append(WeChatConstants.Language_fra);
                } else if("ru".equals(defaultLan)){
                    sbLan.append(WeChatConstants.Language_ru);
                } else if("de".equals(defaultLan)){
                    sbLan.append(WeChatConstants.Language_de);
                } else {
                    sbLan.append(WeChatConstants.Language_en);
                }
            } else {
                sbLan.append(WeChatConstants.Language_en);
            }
            sbLan.append(":").append(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sbLan.toString();
    }

}
