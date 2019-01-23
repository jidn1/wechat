package com.jidn.common.baidu.naturalLang;

import com.baidu.aip.nlp.AipNlp;
import com.jidn.common.util.GlobalConstants;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/23 10:22
 * @Description: baidu npl
 */
public class NaturlLangApi {

        private static String APP_ID ;
        private static String API_KEY ;
        private static String SECRET_KEY ;

        static {
            APP_ID = GlobalConstants.getProperties("baiduNplApi");
            API_KEY = GlobalConstants.getProperties("baiduNplApiKey");
            SECRET_KEY = GlobalConstants.getProperties("baiduNplApiSecretKey");
        }


        public static String SimilarTextNpl(String redisKey,String MsgKey){
            String score = "";
            HashMap<String, Object> options = new HashMap<String, Object>();
            try {
                AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                options.put("model", "CNN");
                JSONObject res = client.simnet(redisKey, MsgKey, options);
                System.out.println(res.toString(2));


            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }




}
