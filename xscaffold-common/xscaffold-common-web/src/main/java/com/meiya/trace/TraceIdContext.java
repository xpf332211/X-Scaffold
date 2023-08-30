package com.meiya.trace;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 放置和拿取traceId
 * @author xiaopf
 */
public class TraceIdContext {


    public static<T> Callable<T> wrap(Callable<T> callable, Map<String,String> contextMap){
        return () -> {
            //如果没有contextMap传递过来，则生成一个新的traceId
            //如果是子线程 则复用传递过来的contextMap
            if (contextMap == null){
                MDC.clear();
            }else {
                MDC.setContextMap(contextMap);
            }
            //没有contextMap传递过来的话 MDC被清除，生成新traceId
            setTraceIdIfAbsent();
            try{
                //执行任务并返回结果
                return callable.call();
            }finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(Runnable runnable, Map<String,String> contextMap){
        return () -> {
            //如果没有contextMap传递过来，则生成一个新的traceId
            //如果是子线程 则复用传递过来的contextMap
            if (contextMap == null){
                MDC.clear();
            }else {
                MDC.setContextMap(contextMap);
            }
            //没有contextMap传递过来的话 MDC被清除，生成新traceId
            setTraceIdIfAbsent();
            try{
                //执行任务
                runnable.run();
            }finally {
                MDC.clear();
            }
        };
    }

    /**
     * 如果MDC中没有traceId 则生成一个并加入
     */
    public static void setTraceIdIfAbsent(){
        if (MDC.get(TraceIdConstant.TRACE_ID_NAME) == null){
            MDC.put(TraceIdConstant.TRACE_ID_NAME,generateTradeId());
        }
    }


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
     * 删除当前线程的MDC的traceId
     */
    public static void removeTraceId(){
        MDC.remove(TraceIdConstant.TRACE_ID_NAME);
    }

    /**
     * 清除当前线程的MDC的所有键值对
     */
    public static void clearTraceId(){
        MDC.clear();
    }

    /**
     * UUID构造tradeId
     * @return 唯一追踪标识
     */
    public static String generateTradeId(){
        return UUID.randomUUID().toString();
    }

}
