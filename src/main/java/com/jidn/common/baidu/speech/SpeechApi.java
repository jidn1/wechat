package com.jidn.common.baidu.speech;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.jidn.common.util.FileUtil;
import com.jidn.common.util.GlobalConstants;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/14 16:43
 * @Description: AipSpeech是语音识别的Java客户端
 */
public class SpeechApi {

    /**
     * 语音合成
     * @param str
     * @param appId
     * @param apiKey
     * @param secretKey
     * @return
     */
    public static String synthesis(String str,String appId,String apiKey,String secretKey) {
        AipSpeech client = new AipSpeech(appId, apiKey, secretKey);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "5");//语速，取值0-9，默认为5中语速      非必选
        options.put("pit", "8");//音调，取值0-9，默认为5中语调      非必选
        options.put("per", "4");//发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女 非必选
        TtsResponse res = client.synthesis(str, "zh", 1, options);
        JSONObject result = res.getResult();
        if (!StringUtils.isEmpty(result)) {
            System.out.printf("error：" + result.toString());
            return result.toString();
        }
        String filePath = GlobalConstants.getProperties("WRITE_FILE_SYSTEM");
        byte[] data = res.getData();
        System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(res.getResult()));
        if (data != null) {
            try {
              return  FileUtil.writeBytesToOssVideo(data,filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String recognition(String src,String appId,String apiKey,String secretKey){
        try {
            AipSpeech client = new AipSpeech(appId, apiKey, secretKey);
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);

            // 调用接口
            JSONObject res = client.asr(src, "amr", 16000, null);
            System.out.println(res.toString(2));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
