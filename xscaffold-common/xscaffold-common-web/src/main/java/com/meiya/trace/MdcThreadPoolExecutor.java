package com.meiya.trace;

import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 * @author xiaopf
 */
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {
    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit seconds, LinkedBlockingDeque<Runnable> workQueue, ThreadFactory defaultThreadFactory, DiscardPolicy discardPolicy) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, seconds, workQueue, defaultThreadFactory, discardPolicy);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(TraceIdContext.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(TraceIdContext.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(TraceIdContext.wrap(task, MDC.getCopyOfContextMap()));
    }

}
