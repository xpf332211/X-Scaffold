package com.meiya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaopf
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "mailThreadPool")
    public ThreadPoolExecutor getMailThreadPool(){
        return new ThreadPoolExecutor(20,50,5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50),
                new CustomThreadFactory("mail"),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
