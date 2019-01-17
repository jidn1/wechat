package com.jidn.wechat.quartz;

import com.jidn.wechat.quartz.job.RobotTask;
import org.apache.log4j.Logger;

import com.jidn.wechat.quartz.job.WeChatTask;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 18:07
 * @Description: QuartzJob
 */
public class QuartzJob {

    private static Logger logger = Logger.getLogger(QuartzJob.class);

    /**
     * 任务执行获取 token
     */
    public void workForToken() {
        try {
            WeChatTask timer = new WeChatTask();
            timer.getToken_getTicket();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void workForInit() {
        try {
            RobotTask timer = new RobotTask();
            timer.Init_Dialogue();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
