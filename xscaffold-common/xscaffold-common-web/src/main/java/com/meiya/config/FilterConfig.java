package com.meiya.config;

import com.meiya.trace.TraceIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * @author xiaopf
 */
@Configuration
public class FilterConfig {

    @Resource
    private TraceIdFilter traceIdFilter;
    @Bean
    public FilterRegistrationBean<TraceIdFilter> filterRegistrationBean(){
        FilterRegistrationBean<TraceIdFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(traceIdFilter);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setName("traceIdFilter");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}
