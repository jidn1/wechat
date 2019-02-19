package com.jidn.common.baidu.naturalLang;

import com.baidu.aip.nlp.AipNlp;
import com.jidn.common.util.GlobalConstants;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/23 10:22
 * @Description: baidu npl
 */
public class NaturlLangApi {

        private static String APP_ID ;
        private static String API_KEY ;
        private static String SECRET_KEY ;
        private static AipNlp client;

        static {
            APP_ID = GlobalConstants.getProperties("baiduNplApi");
            API_KEY = GlobalConstants.getProperties("baiduNplApiKey");
            SECRET_KEY = GlobalConstants.getProperties("baiduNplApiSecretKey");
            client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        }


        public static BigDecimal SimilarTextNpl(String redisKey, String MsgKey){
            String score = "";
            HashMap<String, Object> options = new HashMap<String, Object>();
            try {
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                //默认为"BOW"，可选"BOW"、"CNN"与"GRNN"
                options.put("model", "CNN");
                System.out.println(redisKey+"=============="+MsgKey);
                JSONObject res = client.simnet(redisKey, MsgKey, options);
                System.out.println(res.toString());
                if(res.toString().contains("score")){
                    score = res.get("score").toString();
                } else {
                    score = "0";
                }

                System.out.println("================【使用baidu NPL自然语言处理文本匹配度为:"+score+"】======================");
            } catch (Exception e){
                e.printStackTrace();
            }
            return new BigDecimal(score).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        }


        public static String MoodTextNpl(String text){
            HashMap<String, Object> options = new HashMap<String, Object>();
            try {
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                //default（默认项-不区分场景），talk（闲聊对话-如度秘聊天等），
                // task（任务型对话-如导航对话等），customer_service（客服对话-如电信/银行客服等）
                options.put("scene", "talk");
                JSONObject res = client.emotion(text, options);
                System.out.println(res.toString(2));
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }




}
