package com.jidn.common.util;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/10 17:14
 * @Description: List工具类
 */
public class ListUtil {


    public static String getRandomOne(List<String> lists) {
        Random random = new Random();
        if(null == lists || lists.size() == 0){
            return null;
        }
        return lists.get(random.nextInt(lists.size()));
    }

    public static Object getRandomOne(JSONArray array) {
        Random random = new Random();
        if(null == array || array.size() == 0){
            return null;
        }
       return array.get(random.nextInt(array.size()));
    }


}
