package com.meiya.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import reactor.util.annotation.Nullable;
import java.util.Map;

/**
 * @author xiaopf
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;
    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.APPLICATION_CONTEXT = applicationContext;
    }
    public static ApplicationContext getApplicationContext(){
        return APPLICATION_CONTEXT;
    }
    public static<T> T getBean(Class<T> clazz){
        return APPLICATION_CONTEXT.getBean(clazz);
    }
    public static<T> Map<String, T> getBeansOfType(Class<T> clazz){
        return APPLICATION_CONTEXT.getBeansOfType(clazz);
    }
}
