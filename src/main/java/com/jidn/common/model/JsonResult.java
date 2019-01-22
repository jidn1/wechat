package com.jidn.common.model;

import lombok.Data;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2019/1/15 9:38
 * @Description: json返回消息格式
 */
@Data
public class JsonResult {

    private boolean success = false;

    private Integer code;

    private String result;

}
