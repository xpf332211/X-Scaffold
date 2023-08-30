package com.meiya.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * @author xiaopf
 */
@Component
public class RedisUtil {

    /**
     * 初始化lua脚本
     */
    private DefaultRedisScript<Boolean> casScript;
    @PostConstruct
    public void init(){
        casScript = new DefaultRedisScript<>();
        //lua脚本返回值类型
        casScript.setResultType(Boolean.class);
        //加载位置
        casScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("cas.lua")));
    }


    @Resource
    private RedisTemplate redisTemplate;

    public Boolean compareAndSet(String key,Long oldValue,Long newValue){
        List<String> keys = new ArrayList<>();
        keys.add(key);
        return (Boolean) redisTemplate.execute(casScript,keys,oldValue,newValue);
    }

    public void set(String key,Object object){
        redisTemplate.opsForValue().set(key, object);
    }

    public void set(String key,Object object,long timeout,TimeUnit timeUnit){redisTemplate.opsForValue().set(key,object,timeout,timeUnit);}

    public boolean setNx(String key, String value, Long time, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit));
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }


    public boolean exist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    public boolean del(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public Boolean zAdd(String key, String value, Long score) {
        return redisTemplate.opsForZSet().add(key, value, Double.parseDouble(String.valueOf(score)));
    }

    public Long countZset(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Set<Object> rangeZset(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }
    public Long removeZset(String key, Object value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    public void removeZsetList(String key, Set<String> value) {
        value.forEach((val) -> redisTemplate.opsForZSet().remove(key, val));
    }

    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    public Set<String> rangeByScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScore(key, Double.valueOf(String.valueOf(start)), Double.valueOf(String.valueOf(end)));
    }

    public Object addScore(String key, Object obj, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, obj, score);
    }

    public Object rank(String key, Object obj) {
        return redisTemplate.opsForZSet().rank(key, obj);
    }

}
