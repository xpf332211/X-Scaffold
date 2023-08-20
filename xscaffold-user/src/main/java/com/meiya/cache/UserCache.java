package com.meiya.cache;

import com.meiya.init.AbstractCache;
import com.meiya.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaopf
 */
@Component
public class UserCache extends AbstractCache {

    public static final String USER_KEY_PREFIX = "user:";

    @Resource
    public RedisUtil redisUtil;
    @Override
    public void initCache() {
        //与数据库作联动，缓存数据
        redisUtil.set(USER_KEY_PREFIX + "age",11);
    }

    @Override
    public <T> T getCache(String key) {
        if (!redisUtil.exist(key)){
            reloadCache();
        }
        return (T) redisUtil.get(key);
    }

    @Override
    public void clearCache() {
        redisUtil.del(USER_KEY_PREFIX + "*");
    }

}
