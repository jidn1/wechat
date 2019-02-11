package com.jidn.wechat.quartzJob;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jidn.common.baidu.speech.SpeechApi;
import com.jidn.common.service.RedisService;
import com.jidn.common.util.*;
import com.jidn.wechat.service.SendMessageService;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/24 11:15
 * @Description: 公众号定时任务Job--全部
 */
public class WeChatTask {

    /**
     * @Description: 任务执行体
     * @param @throws Exception
     * @author jidn
     * @date 2018/12/26 18:04
     */
    public void getToken_getTicket() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credential");
        params.put("appid", GlobalConstants.getProperties("appid"));
        params.put("secret", GlobalConstants.getProperties("AppSecret"));
        String jstoken = HttpUtils.sendGet(
                GlobalConstants.getProperties("tokenUrl"), params);
        String access_token = JSONObject.parseObject(jstoken).get("access_token").toString(); // 获取到 token 并赋值保存
        GlobalConstants.App.put("access_token", access_token);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"token 为=============================="+access_token);
    }


    /**
     * @Description: Robot 对话合成
     * @param @throws Exception
     * @author jidn
     * @date 2018/12/30 15:11
     */
    public void Init_Dialogue(){
        try{
            RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
            Map<String, String> map = redisService.hgetAll(WeChatConstants.WECHAT_DIALOGUE_TEM);
            if(null != map){
                long start = System.currentTimeMillis();
                System.out.println("新增机器人对话开始==========================="+System.currentTimeMillis());
                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                Iterator<Map.Entry<String, String>> iter = entrySet.iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    JSONArray OssArray = new JSONArray();
                    String hasVoice = redisService.hget(WeChatConstants.WECHAT_VOICE_FILE_PATH, entry.getKey());
                    if(!StringUtils.isEmpty(hasVoice)){
                        OssArray = JSONObject.parseArray(hasVoice);
                    }
                    System.out.println(entry.getKey()+"================"+entry.getValue()+"\n");
                    JSONArray DiaArray = JSONObject.parseArray(entry.getValue());
                    for(Object vcon : DiaArray){
                        String ossUrl = SpeechApi.synthesis(vcon.toString(), GlobalConstants.getProperties("baiduSpeechApi"),
                                GlobalConstants.getProperties("baiduSpeechApiKey"), GlobalConstants.getProperties("baiduSpeechApiSecretKey"));
                        OssArray.add(ossUrl);
                    }
                    System.out.printf("==========================="+JSONObject.toJSONString(OssArray));
                    redisService.hset(WeChatConstants.WECHAT_VOICE_FILE_PATH,entry.getKey(),JSONObject.toJSONString(OssArray));
                }
                System.out.println("新增机器人对话结束耗时：==========================="+(System.currentTimeMillis()-start));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @Description: clear Robot Thesaurus
     * @param @throws Exception
     * @author jidn
     * @date 2019/01/10 17:44
     */
    public void clear_dia(){
         RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
         redisService.delete(WeChatConstants.WECHAT_DIALOGUE_TEM);
        System.out.println("=================【清除机器人词库】=====================");
    }


    /**
     * 检查微信素材mediaId
     */
    public void inspect_mediaId(){
        try {
            RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
            Map<String, String> mediaIdMap = redisService.hgetAll(WeChatConstants.WECHAT_VOICE);
            //查看素材是否存在 如果不存在  则直接删除key 重新交流时在上传
            Set<Map.Entry<String, String>> entrySet = mediaIdMap.entrySet();
            Iterator<Map.Entry<String, String>> iter = entrySet.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                JSONArray mediaArray = JSONObject.parseArray(entry.getValue());
                for(Object obj : mediaArray){
                    try {
                        boolean exitMediaId = FileUtil.ifHasExitMediaId(GlobalConstants.getProperties("access_token"), obj.toString());
                        System.out.println("检查素材是否存在=====================【"+obj.toString()+"】==============【exitMediaId："+exitMediaId+"】===");
                        if(!exitMediaId){
                            redisService.hdel(WeChatConstants.WECHAT_VOICE,entry.getKey());
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
