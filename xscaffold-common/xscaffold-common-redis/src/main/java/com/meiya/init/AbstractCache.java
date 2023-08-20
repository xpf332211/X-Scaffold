package com.meiya.init;

import org.springframework.stereotype.Component;

/**
 * @author xiaopf
 */
@Component
public abstract class AbstractCache {
    /**
     * 初始化缓存
     */
    public void initCache(){}

    /**
     * 获取缓存内容
     */
    public <T> T getCache(String key){
        return null;
    }

    /**
     * 清除缓存
     */
    public void clearCache(){}

    /**
     * 重新加载缓存
     */
    public void reloadCache(){
        clearCache();
        initCache();
    }
}
