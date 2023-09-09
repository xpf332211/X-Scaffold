package com.meiya.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaopf
 * 未指定或指定不存在 则默认加载caffeine
 */
@Configuration
public class LocalCacheConfig {
    @Bean(name = "caffeine")
    @ConditionalOnProperty(name = "xscaffold.multiLevelCache.util.local", havingValue = "caffeine", matchIfMissing = true)
    public Cache<String,String> caffeineCache(){
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(name = "guava")
    @ConditionalOnProperty(name = "xscaffold.multiLevelCache.util.local", havingValue = "guava")
    public com.google.common.cache.Cache<String,String> guavaCache(){
        return CacheBuilder.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
    }
}
