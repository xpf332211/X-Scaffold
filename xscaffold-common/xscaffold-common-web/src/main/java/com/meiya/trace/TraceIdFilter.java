package com.meiya.trace;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xiaopf
 */
@Component
public class TraceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //约定从请求头中获取traceId
        String traceId = httpServletRequest.getHeader(TraceIdConstant.TRACE_ID_NAME);
        if (StringUtils.isBlank(traceId)){
            traceId = TraceIdContext.generateTradeId();
        }
        //向MDC中装入traceId
        TraceIdContext.setTraceId(traceId);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
