package com.meiya.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author xiaopf
 */
@Slf4j
public class ThreadPoolUtil {

    private ThreadPoolUtil() {
    }

    /**
     * 先平滑关闭，若超过平滑关闭等待时间，则强制关闭，若超过强制关闭超时时间，需要打印异常日志
     * 若外部线程被中断或挂了，需要关闭线程，并需要为其设置标记位
     * @param pool 线程池
     * @param shutdownTimeout 平滑关闭超时时间
     * @param shutdownNowTimeout 立即关闭超时时间
     * @param timeUnit 时间单位
     */
    public static void shutdownPool(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout, TimeUnit timeUnit) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(shutdownTimeout, timeUnit)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(shutdownNowTimeout, timeUnit)) {
                    log.error("ThreadPoolUtils.shutdownPool.error");
                }
            }
        } catch (InterruptedException ie) {
            log.error("ThreadPoolUtils.shutdownPool.interrupted.error:{}", ie.getMessage(), ie);
            pool.shutdownNow();
            //设置了中断状态标志位
            Thread.currentThread().interrupt();
        }
    }

}
