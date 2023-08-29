package com.meiya.trace;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * 放置和拿取traceId
 * @author xiaopf
 */
public class TraceIdContext {


    /**
     * 向MDC中装入traceId
     * @param traceId 唯一追踪标识
     */
    public static void setTraceId(String traceId){
        MDC.put(TraceIdConstant.TRACE_ID_NAME,traceId);
    }

    /**
     * 从MDC中取出traceId
     * @return 唯一追踪标识
     */
    public static String getTraceId(){
        return MDC.get(TraceIdConstant.TRACE_ID_NAME);
    }


    /**
     * UUID构造tradeId
     * @return 唯一追踪标识
     */
    public static String generateTradeId(){
        return UUID.randomUUID().toString();
    }

}
