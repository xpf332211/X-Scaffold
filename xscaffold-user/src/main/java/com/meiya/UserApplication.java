package com.meiya;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xiaopf
 */
@SpringBootApplication(scanBasePackages = "com.meiya")
@MapperScan(basePackages = "com.meiya.mapper")
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true)
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(UserApplication.class,args);
        log.info("user模块启动成功~");
    }
}
