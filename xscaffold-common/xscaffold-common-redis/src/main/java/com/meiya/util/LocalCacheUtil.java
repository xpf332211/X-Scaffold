package com.meiya.util;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author xiaopengfei
 */
@Component
@Slf4j
public class LocalCacheUtil<K,V> {

    @Resource
    private MultiLevelCacheUtil<K,V> multiLevelCacheUtil;
    @Value("${xscaffold.localCache.util.enable:false}")
    private boolean enableLocalCache;
    @Value("${xscaffold.multiLevelCache.util.enable:false}")
    private boolean enableMultiLevelCache;
    private final Cache<String,String> localCache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    /**
     *  结合本地缓存获取结果
     * @param idList 需要查询的id集合
     * @param keyPrefix key前缀
     * @param clazz 实体类类型
     * @param function 实际查询的函数式接口
     * @return <id,info> 结果map
     */
    public Map<K,V> getResult(List<K> idList, String keyPrefix, Class<V> clazz, Function<List<K>,Map<K,V>> function){
        if (enableMultiLevelCache){
            log.warn("您已开启多级缓存，默认使用多级缓存");
            return multiLevelCacheUtil.getResult(idList, keyPrefix, clazz, function, 60 * 60, TimeUnit.SECONDS);
        }
        if (CollectionUtils.isEmpty(idList)){
            return Collections.emptyMap();
        }
        Map<K,V> resultMap = new HashMap<>(16);
        //没有开启本地缓存
        if (!enableLocalCache){
            log.info("未开启本地缓存");
            resultMap = function.apply(idList);
            return resultMap;
        }
        List<K> noCacheIdList = new ArrayList<>();
        for (K id : idList){
            String cacheKey = keyPrefix + ":" + id;
            String info = localCache.getIfPresent(cacheKey);
            //本地缓存中有
            if (StringUtils.isNotBlank(info)){
                V valueInfo = JSON.parseObject(info,clazz);
                resultMap.put(id,valueInfo);
                log.info("本地缓存中存在id为【{}】的信息",id);
            }else {
                //对于缓存中查询不到的信息，将其id加入到待查询列表中
                noCacheIdList.add(id);
            }
        }
        //全部都在缓存中
        if (CollectionUtils.isEmpty(noCacheIdList)){
            log.info("查询结果全部在缓存中");
            return resultMap;
        }
        //实际查询
        Map<K, V> noCacheValueInfo = function.apply(noCacheIdList);
        //未查到
        if (noCacheValueInfo == null || noCacheValueInfo.isEmpty()){
            return resultMap;
        }
        for (Map.Entry<K,V> entry : noCacheValueInfo.entrySet()){
            K id = entry.getKey();
            V info = entry.getValue();
            resultMap.put(id,info);
            //新查询出来的 加入本地缓存
            String cacheKey = keyPrefix + ":" + id;
            localCache.put(cacheKey,JSON.toJSONString(info));
            log.info("id为【{}】的信息加入到了本地缓存",id);
        }
        return resultMap;
    }
}
