package com.meiya.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author xiaopengfei
 */
@Component
@Slf4j
public class LocalCacheUtil<K,V> {

    @Value("${xscaffold.localCache.enable}")
    private boolean enableLocalCache;
    private Cache<String,String> localCahe = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build();

    /**
     *  结合缓存获取结果
     * @param idList
     * @param keyPrefix
     * @param clazz
     * @param function
     * @return
     */
    public Map<K,V> getResult(List<K> idList, String keyPrefix, Class<V> clazz, Function<List<K>,Map<K,V>> function){
        if (CollectionUtils.isEmpty(idList)){
            return Collections.emptyMap();
        }
        Map<K,V> resultMap = new HashMap<>(8);
        //没有开启本地缓存
        if (!enableLocalCache){
            resultMap = function.apply(idList);
            return resultMap;
        }
    }
}
