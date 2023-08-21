package com.meiya.util;

import com.meiya.exception.DistributedLockException;
import io.netty.bootstrap.Bootstrap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于setNx的分布式锁 大多数情况下不会有问题
 *
 * @author xiaopf
 */
@Component
public class RedisDistributedLockUtil {
    @Resource
    private RedisUtil redisUtil;

    private static final Long TIME_OUT = 1000L;

    private static final String ID_PREFIX = UUID.randomUUID() + "-";

    /**
     *  可重试获取锁
     * @param lockKey 锁key
     * @param expireTimeSec 锁过期时间
     * @return 成功与否
     */
    public boolean lock(String lockKey, Long expireTimeSec) {
        if (StringUtils.isBlank(lockKey) || expireTimeSec < 0) {
            throw new DistributedLockException("获取分布式锁时发生异常");
        }
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        long currentTime = System.currentTimeMillis();
        //最大重试时间
        long retryEndTime = currentTime + TIME_OUT;
        boolean success = false;
        //自旋
        while (currentTime < retryEndTime && !success) {
            //获取锁
            success = redisUtil.setNx(lockKey, threadId, expireTimeSec, TimeUnit.SECONDS);
            if (!success) {
                try {
                    Thread.sleep(50 + new Random().nextInt(50));
                } catch (InterruptedException e) {
                    throw new DistributedLockException("获取分布式锁时发生异常");
                }
            }
            currentTime = System.currentTimeMillis();
        }
        return success;
    }

    /**
     * 释放锁
     * 在极端情况下，线程A判断锁是自己的，还没执行释放，锁刚好到期，其他线程拿到锁，
     * 此时线程A执行释放锁，便把其他线程的锁给删掉了
     * @return 成功与否
     */
    public boolean unlock(String lockKey) {
        if (StringUtils.isBlank(lockKey)) {
            throw new DistributedLockException("获取分布式锁时发生异常");
        }
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        String id = redisUtil.get(lockKey);
        //锁是自己的 释放 判断+解锁操作不是原子性，极端情况下有问题
        if (threadId.equals(id)){
            return redisUtil.del(lockKey);
        }
        return false;
    }

    /**
     * 尝试加锁
     * 直接返回结果
     * @return 成功与否
     */
    public boolean tryLock(String lockKey,long expireTimeSec) {
        if (StringUtils.isBlank(lockKey) || expireTimeSec < 0) {
            throw new DistributedLockException("获取分布式锁时发生异常");
        }
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        return redisUtil.setNx(lockKey,threadId,expireTimeSec,TimeUnit.SECONDS);
    }
}
