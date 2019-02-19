package com.jidn.common.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2019/1/22 10:27
 * @Description:
 */
public class QuartzJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
            TaskUtils.invokMethod(scheduleJob);
        } catch (Exception e) {
        }
    }
}
