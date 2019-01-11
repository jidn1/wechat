package com.jidn.common.util;

import java.util.Properties;
/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 15:16
 * Description:
 */
public class GlobalConstants {

    public static Properties interfaceUrlProperties;


    /**
     *
     * @Description: TODO
     * @param @param key
     * @param @return
     * @author jidn
     * @date 2018/12/26 15:16
     */
    public static String getInterfaceUrl(String key) {
        return (String) interfaceUrlProperties.get(key);
    }

}
