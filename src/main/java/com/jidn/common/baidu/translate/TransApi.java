package com.jidn.common.baidu.translate;

import com.jidn.common.util.HttpUtils;
import com.jidn.common.util.MD5;
import com.jidn.common.util.WeChatConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/11 9:55
 * @Description:百度翻译API
 */
public class TransApi {

    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String to) {
        Map<String, String> params = buildParams(query, "auto", to);
        return HttpUtils.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", getLanguage(to));
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }

    public String getLanguage(String to){
        String ifHasLan = to.split(":")[0];
        String toLan = "";
        try{
            if(ifHasLan.length() >= 4){
                String Language = ifHasLan.substring(3,5);
                if(WeChatConstants.Language_jp.equals(Language)){
                    toLan = "jp";
                } else if(WeChatConstants.Language_kor.equals(Language)){
                    toLan = "kor";
                } else if(WeChatConstants.Language_fra.equals(Language)){
                    toLan = "fra";
                } else if(WeChatConstants.Language_ru.equals(Language)){
                    toLan = "ru";
                } else if(WeChatConstants.Language_de.equals(Language)){
                    toLan = "de";
                }
            } else {
                toLan = "en";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return toLan;
    }




    public static void main(String[] args) {
        String APP_ID = "20190111000256167";
        String SECURITY_KEY = "Vk06GyVtmEWgLkZyx6b8";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        try {
            String query = "人生一直是如此痛苦";
            String transResult = api.getTransResult(query, "jp");
            String res = new String(transResult.getBytes(), "UTF-8");
            System.out.println(res);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
