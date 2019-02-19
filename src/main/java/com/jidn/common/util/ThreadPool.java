package com.jidn.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/15 10:31
 * @Description: 线程池
 */
public class ThreadPool {

    private static ExecutorService executors = Executors.newFixedThreadPool(1000);

    public static ExecutorService getExecutor(){
        return executors;
    }

    public static void exe(Runnable run){
        executors.execute(run);
    };
}
