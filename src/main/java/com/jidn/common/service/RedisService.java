package com.jidn.common.service;

import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/28 16:23
 * @Description:
 */
public abstract interface RedisService {

    public abstract String get(String paramString);

    public abstract String save(String paramString1, String paramString2);

    public abstract void save(String paramString1, String paramString2, int paramInt);

    public abstract Long delete(String paramString);

    public abstract void setTime(String paramString, int paramInt);

    public abstract void saveMap(String paramString, Map<String, String> paramMap);

    public abstract void hset(String paramString1, String paramString2, String paramString3);

    public abstract String hget(String paramString1, String paramString2);

    public abstract String hdel(String paramString1, String paramString2);

    public abstract Long getKeyTime(String paramString);

    public abstract Map<String, String> hgetAll(String paramString);


}
