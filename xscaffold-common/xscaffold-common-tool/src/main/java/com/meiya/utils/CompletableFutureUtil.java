package com.meiya.utils;

import org.slf4j.Logger;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaopf
 */

public class CompletableFutureUtil {

    public static <T> T getResult(Future<T> future, long timeout, TimeUnit timeUnit,
                                  T defaultValue, Logger log){

        try{
            return future.get(timeout,timeUnit);
        }catch (Exception e){
            log.error("CompletableFuture.getResult.error【{}】",e.getMessage(),e);
            return defaultValue;
        }
    }
}
