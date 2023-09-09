package com.meiya.bean;

import com.google.common.cache.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaopf
 */
@Component
@ConditionalOnProperty(name = "xscaffold.multiLevelCache.util.local", havingValue = "guava")
public class GuavaCache implements LocalCache<String>{

    @Resource(name = "guava")
    private Cache<String,String> guavaCache;

    @Override
    public String getIfPresent(String key) {
        return guavaCache.getIfPresent(key);
    }

    @Override
    public void put(String key, String value) {
        guavaCache.put(key,value);
    }


}
