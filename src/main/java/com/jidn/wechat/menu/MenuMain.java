package com.jidn.wechat.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jidn.common.util.HttpUtils;
import lombok.Data;


/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/29 16:00
 * @Description:
 */
public class MenuMain {

    public static void main(String[] args) {

        ClickButton cbt=new ClickButton();
        cbt.setKey("image");
        cbt.setName("回复图片");
        cbt.setType("click");

        ViewButton vbt=new ViewButton();
        vbt.setUrl("http://www.zjjtv.top");
        vbt.setName("博客");
        vbt.setType("view");

        JSONArray sub_button=new JSONArray();
        sub_button.add(cbt);
        sub_button.add(vbt);

        JSONObject buttonOne=new JSONObject();
        buttonOne.put("name", "菜单");
        buttonOne.put("sub_button", sub_button);

        JSONArray button=new JSONArray();
        button.add(vbt);
        button.add(buttonOne);
        button.add(cbt);

        JSONObject menujson=new JSONObject();
        menujson.put("button", button);
        System.out.println(menujson);
        String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=17_aRA4YyZZH6NBrCb-oYAvbR0KHGtJQ6CMVta3sqXdKYn1xtmy4m6KtoAul4qF9m3s38jHXlHFF5BmKp0Ogme5fu1f2ibbJ3zxZctLwGr4yLD5PYRpOs86t5rJ8QaNxsJ_BU7X6JGezsC5GZtULDPiADASKW";
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("access_token",
//                GlobalConstants.getProperties("access_token"));  //定时器中获取到的 token
        try{
            String rs=HttpUtils.sendPostBuffer(url, menujson.toJSONString());
            System.out.println(rs);
        }catch(Exception e){
            System.out.println("请求错误！");
        }

    }



    public String menuUrl(){


        return null;
    }

}
