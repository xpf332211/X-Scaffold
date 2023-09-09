package com.meiya.bean;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Callable;

/**
 * @author xiaopf
 */
@Component
@ConditionalOnProperty(name = "xscaffold.multiLevelCache.util.local", havingValue = "caffeine", matchIfMissing = true)
public class CaffeineCache implements LocalCache<String>{

    @Resource(name = "caffeine")
    private Cache<String,String> caffeineCache;
    @Override
    public String getIfPresent(String key) {
        return caffeineCache.getIfPresent(key);
    }


    @Override
    public void put(String key, String value) {
        caffeineCache.put(key,value);
    }
}
