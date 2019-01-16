package com.jidn.common.util;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/26 15:16
 * Description:
 */
public class GlobalConstants {

    public static Properties App;


    static {
        App = new Properties();
        try {
            App.load(new FileReader(GlobalConstants.class
                    .getClassLoader()
                    .getResource("wechat.properties")
                    .getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Description: TODO
     * @param @param key
     * @param @return
     * @author jidn getProperties
     * @date 2018/12/26 15:16
     */
    public static String getProperties(String key) {
        return (String) App.get(key);
    }

}
