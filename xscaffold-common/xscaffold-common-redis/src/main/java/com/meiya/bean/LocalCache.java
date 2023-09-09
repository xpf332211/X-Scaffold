package com.meiya.bean;

/**
 * @author xiaopf
 */
public interface LocalCache<T> {
    /**
     * 若key不存在获取缓存
     * @param key key
     * @return 泛型缓存对象
     */
    T getIfPresent(String key);


    /**
     * 加入缓存
     * @param key key
     * @param value value
     */
    void put(String key,T value);
}
