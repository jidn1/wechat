package com.jidn.wechat.quartz.job;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.SpringUtil;
import com.jidn.common.util.WeChatConstants;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/18 19:39
 * Description:
 */
public class RedisTask {


    public void clear_dia(){
        RedisService redisService = (RedisService) SpringUtil.getBean("redisService");
        redisService.delete(WeChatConstants.WECHAT_DIALOGUE_TEM);
        System.out.println("=================【清除机器人词库】=====================");
    }
}
