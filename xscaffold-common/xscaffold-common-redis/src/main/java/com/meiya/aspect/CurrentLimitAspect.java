package com.meiya.aspect;

import com.meiya.anotation.CurrentLimit;
import com.meiya.exception.CurrentLimitException;
import com.meiya.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaopf
 */
@Slf4j
@Aspect
@Component
public class CurrentLimitAspect {
    @Resource
    private RedisTemplate<String,Long> redisTemplate;

    @Pointcut("@annotation(com.meiya.anotation.CurrentLimit)")
    private void pointCut(){}

    /**
     * 加载lua脚本
     */
    private DefaultRedisScript<Long> currentLimitScript;
    @PostConstruct
    public void init(){
        currentLimitScript = new DefaultRedisScript<>();
        currentLimitScript.setResultType(Long.class);
        currentLimitScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("current-limiting.lua")));
    }

    @Before("pointCut()")
    public void currentLimit(JoinPoint jp) throws CurrentLimitException {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();

        //获取方法上的限流注解
        CurrentLimit annotation = method.getAnnotation(CurrentLimit.class);
        if (annotation != null){
            //该方法所在类的名称
            String className = method.getDeclaringClass().getName();
            //方法名称
            String methodName = method.getName();
            //注解中的key
            String keyPrefix = annotation.key();
            //拼接完整的redis的key
            String key = keyPrefix + ":" + className + ":" + methodName;
            long limitPerUnitTime = annotation.limitPerUnitTime();
            long unitTime = annotation.unitTime();
            String message = annotation.message();
            List<String> keys = new ArrayList<>();
            keys.add(key);
            Long currentCount = redisTemplate.execute(currentLimitScript, keys, limitPerUnitTime,unitTime);
            if (currentCount != null && currentCount == 0){
                log.info("发生限流");
                throw new CurrentLimitException(message);
            }
        }
    }

}
