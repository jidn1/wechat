package com.jidn.web.aop;

import com.jidn.common.service.RedisService;
import com.jidn.common.util.WeChatConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/22 15:13
 * @Description: 语音次数限制
 */
@Aspect
@Component
public class VoiceAspect {

    @Resource
    private RedisService redisService;

    @Pointcut("execution(* com.jidn.wechat.service.impl.SendMessageServiceImpl.sendMessageVoice(..))")
    private void pointCut(){}

    @Around("pointCut()")
    public Object doAfterReturning(ProceedingJoinPoint pjp) throws Throwable {
        Integer countIter = 1;
        Object[] args = pjp.getArgs();
        String content = args[0].toString();
        String openid = args[1].toString();
        String count = redisService.get(WeChatConstants.WECHAT_SAME +":"+openid+":"+content);
        if(!StringUtils.isEmpty(count)){
            countIter = Integer.valueOf(count);
            if(countIter < 3){
                countIter ++ ;
                redisService.save(WeChatConstants.WECHAT_SAME+":"+openid+":"+content,countIter.toString(),60*2);
            } else {
                redisService.delete(WeChatConstants.WECHAT_SAME+":"+openid+":"+content);
                args[0] = WeChatConstants.WECHAT_COUNT_MORE;
            }
        } else {
            redisService.save(WeChatConstants.WECHAT_SAME+":"+openid+":"+content,countIter.toString(),60*2);
        }
        Object proceed = pjp.proceed(args);
        return proceed;
    }
}
