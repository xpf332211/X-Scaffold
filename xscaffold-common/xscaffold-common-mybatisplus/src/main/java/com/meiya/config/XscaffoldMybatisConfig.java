package com.meiya.config;

import com.meiya.interceptor.XscaffoldSqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaopf
 */
@Configuration
public class XscaffoldMybatisConfig {
    @Bean
    public XscaffoldSqlInterceptor sqlInterceptor(){
        return new XscaffoldSqlInterceptor();
    }
}
