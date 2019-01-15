package com.jidn.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/15 9:52
 * @Description: 日期处理类
 */
public class DateUtil {

    /**
     * 获取前一天或前几个小时
     *
     * @param dayOrHour 1代表的是对年份操作，2是对月份操作，3是对星期操作，5是对日期操作，11是对小时操作，12是对分钟操作，13是对秒操作，14是对毫秒操作
     * @param num      要操作的数
     * @return
     */
    public static String getBeforeTime(Integer dayOrHour, Integer num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dayOrHour, -num);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
     * 获取后一天或后几个小时
     *
     * @param dayOrHour 1代表的是对年份操作，2是对月份操作，3是对星期操作，5是对日期操作，11是对小时操作，12是对分钟操作，13是对秒操作，14是对毫秒操作
     * @param num      要操作的数
     * @return
     */
    public static String getLaterTime(Integer dayOrHour, Integer num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dayOrHour, +num);
        date = calendar.getTime();
        return sdf.format(date);
    }


    public static void main(String[] args) {
        System.out.println(getLaterTime(11,1));
    }
}
