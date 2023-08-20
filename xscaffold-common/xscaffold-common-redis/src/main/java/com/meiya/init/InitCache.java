package com.meiya.init;

import com.meiya.util.SpringContextUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaopf
 * 在springboot启动后执行
 */
@Component
public class InitCache implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //将继承AbstractCache类的bean进行缓存预热
        Map<String, AbstractCache> beanMap = SpringContextUtil.getBeansOfType(AbstractCache.class);
        if (beanMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String,AbstractCache> entry : beanMap.entrySet()){
            AbstractCache cache = SpringContextUtil.getBean(entry.getValue().getClass());
            cache.initCache();
        }
    }
}
