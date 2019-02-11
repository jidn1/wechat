package com.jidn.web.listener;

import com.jidn.common.quartz.QuartzJob;
import com.jidn.common.quartz.QuartzManager;
import com.jidn.common.quartz.ScheduleJob;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/22 10:46
 * @Description: 全局监听器
 */
public class StartupListener extends ContextLoaderListener {
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        try {

            ScheduleJob WeChatTokenJob = new ScheduleJob();
            WeChatTokenJob.setBeanClass("com.jidn.wechat.quartzJob.WeChatTask");
            WeChatTokenJob.setMethodName("getToken_getTicket");
            QuartzManager.addJob("WeChatTokenJob", WeChatTokenJob, QuartzJob.class, "0 0/1 * * * ?");

            ScheduleJob RobotJob = new ScheduleJob();
            RobotJob.setBeanClass("com.jidn.wechat.quartzJob.WeChatTask");
            RobotJob.setMethodName("Init_Dialogue");
            QuartzManager.addJob("RobotJob", RobotJob, QuartzJob.class, " 0 30 0 * * ?");

            ScheduleJob ClearRobotJob = new ScheduleJob();
            ClearRobotJob.setBeanClass("com.jidn.wechat.quartzJob.WeChatTask");
            ClearRobotJob.setMethodName("clear_dia");
            QuartzManager.addJob("ClearRobotJob", ClearRobotJob, QuartzJob.class, "0 0 1 * * ?");

            ScheduleJob InspectMediaIdJob = new ScheduleJob();
            InspectMediaIdJob.setBeanClass("com.jidn.wechat.quartzJob.WeChatTask");
            InspectMediaIdJob.setMethodName("inspect_mediaId");
            QuartzManager.addJob("InspectMediaIdJob", InspectMediaIdJob, QuartzJob.class, "0 30 1 * * ?");



        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
