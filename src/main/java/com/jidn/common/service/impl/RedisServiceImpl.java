package com.jidn.common.service.impl;

import com.jidn.common.service.RedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Copyright © 北京互融时代软件有限公司
 * @Author: Jidn
 * @Date: 2018/12/28 16:45
 * @Description:RedisServiceImpl
 */
public class RedisServiceImpl implements RedisService {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool pool;
    @Resource(name = "jedisPool")
    private JedisPool jedisPool;

    private ShardedJedis getResource() {
        return this.pool.getResource();
    }

    @Override
    public String get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            return jedis.get(key);
        } catch (Exception e) {
            returnResource(this.pool, jedis);
            e.printStackTrace();
            return "";
        } finally {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public String save(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            returnResource(this.pool, jedis);
            e.printStackTrace();
            return "";
        } finally {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public void save(String key, String value, int time) {
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key,value);
            jedis.expire(key,time);
        } catch (Exception e) {
            returnResource(this.pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public Long delete(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(key);
        } catch (Exception e) {
            returnResource(this.pool, jedis);
            e.printStackTrace();
            return Long.valueOf(0L);
        } finally {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public void setTime(String paramString, int paramInt) {
        ShardedJedis jedis = null;
        try{
            jedis = getResource();
            jedis.expire(paramString, paramInt);
        }catch (Exception e){
            returnResource(this.pool, jedis);
            e.printStackTrace();
        }
        finally
        {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public void saveMap(String paramString, Map<String, String> paramMap) {
        ShardedJedis jedis = null;
        try
        {
            jedis = getResource();
            jedis.hmset(paramString, paramMap);
        }
        catch (Exception e) {}finally
        {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public void hset(String paramString1, String paramString2, String paramString3) {
        ShardedJedis jedis = null;
        try
        {
            jedis = getResource();
            jedis.hset(paramString1, paramString2, paramString3);
        }
        catch (Exception e) {}finally
        {
            returnResource(this.pool, jedis);
        }
    }

    @Override
    public String hget(String paramString1, String paramString2) {
        ShardedJedis jedis = null;
        try
        {
            jedis = getResource();
            String ss = jedis.hget(paramString1, paramString2);
            return ss;
        }
        catch (Exception e) {}finally
        {
            returnResource(this.pool, jedis);
        }
        return null;
    }

    @Override
    public String hdel(String paramString1, String paramString2) {
        ShardedJedis jedis = null;
        try
        {
            jedis = getResource();
            String ss = jedis.hget(paramString1, paramString2);
            return ss;
        }
        catch (Exception e) {}finally
        {
            returnResource(this.pool, jedis);
        }
        return null;
    }

    @Override
    public Long getKeyTime(String paramString) {
        try
        {
            Long l = getResource().ttl(paramString);
            Long localLong1 = l;return localLong1;
        }
        catch (Exception e)
        {
            e = e;
        }
        finally {}
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String paramString) {
        ShardedJedis jedis = null;
        try
        {
            jedis = getResource();
            Map<String, String> map = jedis.hgetAll(paramString);
            return map;
        }
        catch (Exception e) {}finally
        {
            returnResource(this.pool, jedis);
        }
        return null;
    }

    public void returnResource(ShardedJedisPool pool, ShardedJedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }
}
