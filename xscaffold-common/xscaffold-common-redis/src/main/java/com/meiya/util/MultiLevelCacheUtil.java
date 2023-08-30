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
 * @author xiaopf
 */
@Slf4j
@Component
public class MultiLevelCacheUtil<K,V> {
    @Value("${xscaffold.multiLevelCache.util.enable:false}")
    private boolean enableMultiLevelCache;
    private final Cache<String,String> localCache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();
    @Resource
    private RedisUtil redisUtil;

    /**
     *  结合本地缓存获取结果
     * @param idList 需要查询的id集合
     * @param keyPrefix key前缀
     * @param clazz 实体类类型
     * @param function 实际查询的函数式接口
     * @return <id,info> 结果map
     */
    public Map<K,V> getResult(List<K> idList, String keyPrefix, Class<V> clazz, Function<List<K>,Map<K,V>> function,
                              long expireTime,TimeUnit timeUnit){
        if (CollectionUtils.isEmpty(idList)){
            return Collections.emptyMap();
        }
        Map<K,V> resultMap = new HashMap<>(16);
        //没有开启多级缓存
        if (!enableMultiLevelCache){
            log.info("未开启多级缓存");
            resultMap = function.apply(idList);
            return resultMap;
        }
        List<K> noRedisCacheIdList = new ArrayList<>();
        List<K> noLocalCacheIdList = new ArrayList<>();
        //redis缓存
        for (K id : idList){
            String cacheKey = keyPrefix + ":" + id;
            String info = redisUtil.get(cacheKey);
            //redis中有
            if (StringUtils.isNotBlank(info)){
                V valueInfo = JSON.parseObject(info, clazz);
                resultMap.put(id,valueInfo);
                log.info("redis缓存中存在id为【{}】的信息",id);
            }else {
                //redis缓存中查不到的信息，将其id加入到待查询列表中
                noRedisCacheIdList.add(id);
            }
        }
        //全部都在redis缓存中
        if (CollectionUtils.isEmpty(noRedisCacheIdList)){
            log.info("查询结果全部在redis缓存中");
            return resultMap;
        }
        //本地缓存
        for (K id : noRedisCacheIdList){
            String cacheKey = keyPrefix + ":" + id;
            String info = localCache.getIfPresent(cacheKey);
            //本地缓存中有
            if (StringUtils.isNotBlank(info)){
                V valueInfo = JSON.parseObject(info,clazz);
                resultMap.put(id,valueInfo);
                log.info("本地缓存中存在id为【{}】的信息",id);
                //加入到redis缓存
                redisUtil.set(cacheKey,info,getExpireTime(expireTime),timeUnit);
                log.info("id为【{}】的信息加入到了redis缓存",id);
            }else {
                //对于本地缓存中查询不到的信息，将其id加入到待查询列表中
                noLocalCacheIdList.add(id);
            }
        }
        //全部都在缓存中
        if (CollectionUtils.isEmpty(noLocalCacheIdList)){
            log.info("redis未查到的结果，全部在本地缓存中");
            return resultMap;
        }
        //实际查询
        Map<K, V> noCacheValueInfo = function.apply(noLocalCacheIdList);
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
            String jsonInfo = JSON.toJSONString(info);
            localCache.put(cacheKey,jsonInfo);
            log.info("id为【{}】的信息加入到了本地缓存",id);
        }
        return resultMap;
    }

    /**
     * 生成redis key的过期时间 1小时+一分钟内的随机时间
     * @return key过期时间
     */
    private Long getExpireTime(long expireTime){
        Random random = new Random();
        // 生成 -60 到 60 之间的随机数
        long randomNumber = random.nextLong() % 61;
        if (randomNumber < 0) {
            // 转换为 0 到 60 之间的正数
            randomNumber += 61;
        }
        return randomNumber + expireTime;
    }
}
