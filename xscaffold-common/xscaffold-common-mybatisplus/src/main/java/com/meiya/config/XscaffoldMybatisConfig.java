package com.meiya.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.meiya.interceptor.XscaffoldSqlInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaopf
 */
@Configuration
public class XscaffoldMybatisConfig {
    @Bean
    @ConditionalOnProperty(name = {"xscaffold.sql.graceful.show"},havingValue = "true",matchIfMissing = true)
    public XscaffoldSqlInterceptor sqlInterceptor(){
        return new XscaffoldSqlInterceptor();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
