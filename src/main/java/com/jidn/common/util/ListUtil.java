package com.jidn.common.util;

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


    public static void getRandomOne(List<Object> lists) {
        Random random = new Random();
        lists.get(random.nextInt(lists.size()));
    }


}
