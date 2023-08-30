package com.meiya.trace;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaopf
 */
@Component
public class TraceIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //约定从请求头中获取traceId
        String traceId = request.getHeader(TraceIdConstant.TRACE_ID_NAME);
        if (StringUtils.isBlank(traceId)){
            traceId = TraceIdContext.generateTradeId();
        }
        //向MDC中装入traceId
        TraceIdContext.setTraceId(traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TraceIdContext.removeTraceId();
    }
}
