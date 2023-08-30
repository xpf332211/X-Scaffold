package com.meiya.config;

import com.meiya.trace.MdcThreadPoolExecutor;
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
    //最大可用的CPU核数
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Bean(name = "mailThreadPool")
    public ThreadPoolExecutor getMailThreadPool(){
        return new ThreadPoolExecutor(PROCESSORS * 2,
                PROCESSORS * 4,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50),
                new CustomThreadFactory("mail"),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    @Bean(name = "mdcThreadPool")
    public ThreadPoolExecutor getMdcThreadPoolExecutor(){
        return new MdcThreadPoolExecutor(PROCESSORS * 2,
                PROCESSORS * 4,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
