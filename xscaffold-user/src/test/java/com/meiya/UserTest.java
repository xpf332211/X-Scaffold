package com.meiya;

import com.alibaba.fastjson.JSON;
import com.meiya.entity.po.UserPo;
import com.meiya.event.Person;
import com.meiya.event.PersonEventService;
import com.meiya.queue.delay.MassEmailTask;
import com.meiya.queue.delay.MassEmailTaskService;
import com.meiya.service.UserService;
import com.meiya.util.RedisDistributedLockUtil;
import com.meiya.utils.CompletableFutureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = UserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class UserTest {

    @Resource
    private UserService userService;

    @Resource(name = "mailThreadPool")
    private ThreadPoolExecutor mailThreadPool;

    @Resource
    private PersonEventService personEventService;

    @Resource
    private MassEmailTaskService massEmailTaskService;

    @Resource
    private RedisDistributedLockUtil redisDistributedLockUtil;

    @Test
    public void test01() {
        UserPo userPo = userService.getById(1);
        System.out.println(userPo);
    }

    @Test
    public void test02() {
        //代理对象的类型
        Class<? extends UserService> aClass = userService.getClass();
        //获取代理对象的目标类
        Class<?> targetClass = AopUtils.getTargetClass(userService);
        System.out.println(aClass);
        System.out.println(targetClass);
    }

    @Test
    public void test03() {
        for (int i = 0; i < 10; i++) {
            mailThreadPool.execute(() -> {
                log.info("111");
            });
        }
    }

    @Test
    public void test04() {
        List<FutureTask<String>> futureTaskList = new ArrayList<>();
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            return "result1";
        });
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return "result2";
        });

        futureTaskList.add(futureTask1);
        futureTaskList.add(futureTask2);

        mailThreadPool.submit(futureTask1);
        mailThreadPool.submit(futureTask2);

        for (FutureTask<String> futureTask : futureTaskList) {
            String result = CompletableFutureUtil.getResult(futureTask, 1L, TimeUnit.SECONDS,
                    "defaultResult", log);
            log.info("result:【{}】", result);
        }
    }

    @Test
    public void test05() {
        Person person = new Person(1L, "jerry", 18);
        personEventService.createPerson(person);
    }

    /**
     * 将任务加入延时队列中
     */
    @Test
    public void test06() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long executeTimeStamp = System.currentTimeMillis() + 1000L * 30;
        String executeTimeStr = dateFormat.format(new Date(executeTimeStamp));
        Date executeTime = dateFormat.parse(executeTimeStr);
        MassEmailTask massEmailTask = new MassEmailTask(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE, executeTime);
        massEmailTaskService.pushTaskToQueue(massEmailTask);
    }

    /**
     * 将任务从延时队列中拉取
     */
    @Test
    public void test07() {
        String lockKey = "delay:task:key";
        try {
            boolean lock = redisDistributedLockUtil.lock(lockKey, 3600L);
            if (lock) {
                Set<Long> ids = massEmailTaskService.pullTaskFromQueue();
                log.info("拉取到任务id集合：【{}】", JSON.toJSONString(ids));
                //执行真正的业务逻辑
            }
        } catch (Exception e) {
            log.error("拉取任务时发生异常【{}】", e.getMessage(), e);
        } finally {
            redisDistributedLockUtil.unlock(lockKey);
        }
    }


}
