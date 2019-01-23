package com.jidn.common.util;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/10 17:14
 * @Description: List工具类
 */
public class ListUtil {


    public static String getRandomOne(List<String> lists) {
        Random random = new Random();
        return lists.get(random.nextInt(lists.size()));
    }

    public static Object getRandomOne(JSONArray array) {
        Random random = new Random();
       return array.get(random.nextInt(array.size()));
    }


}
