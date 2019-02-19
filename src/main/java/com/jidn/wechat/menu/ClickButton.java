package com.jidn.wechat.menu;

import lombok.Data;

/**
 * @Copyright © 正经吉
 * @Author: Jidn
 * @Date: 2018/12/29 16:00
 * @Description: 点击型菜单事件
 */
@Data
public class ClickButton {

    private String type;
    private String name;
    private String key;

}
