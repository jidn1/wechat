package com.jidn.wechat.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jidn.common.util.WeChatConstants;
import com.jidn.wechat.dispatcher.EventDispatcher;
import com.jidn.wechat.dispatcher.MsgDispatcher;
import com.jidn.common.util.MessageUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jidn.common.util.SignUtil;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 15:45
 * @Description:
 */
@Controller
@RequestMapping("/wechat")
public class WechatSecurityController {

    private static Logger logger = Logger.getLogger(WechatSecurityController.class);

    /**
     *
     * @Description: 用于接收 get 参数，返回验证参数
     * @param @param request
     * @param @param response
     * @param @param signature
     * @param @param timestamp
     * @param @param nonce
     * @param @param echostr
     * @author jidn
     * @date 2018/12/26 15:45
     */
    @RequestMapping(value = "security", method = RequestMethod.GET)
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("这里存在非法请求！");
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /**
     * @Description: 接收微信端消息处理并做分发
     * @param @param request
     * @param @param response
     * @author jidn
     * @date 2018/12/26 15:45
     */
    @RequestMapping(value = "security", method = RequestMethod.POST)
    public void DoPost(HttpServletRequest request,HttpServletResponse response) {
        try{
            Map<String, String> map=MessageUtil.parseXml(request);
            String msgtype=map.get(WeChatConstants.MSG_TYPE);
            String respXML = "";
            if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)){
                respXML = EventDispatcher.processEvent(map); //进入事件处理
            }else{
                respXML = MsgDispatcher.processMessage(map); //进入消息处理
            }
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(respXML);
            out.flush();
            out.close();
        }catch(Exception e){
            logger.error(e,e);
        }
    }
}
