package com.meiya;

import com.meiya.entity.po.UserPo;
import com.meiya.event.Person;
import com.meiya.event.PersonEventService;
import com.meiya.service.UserService;
import com.meiya.utils.CompletableFutureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = UserApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class UserTest {

    @Resource
    private UserService userService;

    @Resource(name = "mailThreadPool")
    private ThreadPoolExecutor mailThreadPool;

    @Resource
    private PersonEventService personEventService;

    @Test
    public void test01(){
        UserPo userPo = userService.getById(1);
        System.out.println(userPo);
    }

    @Test
    public void test02(){
        //代理对象的类型
        Class<? extends UserService> aClass = userService.getClass();
        //获取代理对象的目标类
        Class<?> targetClass = AopUtils.getTargetClass(userService);
        System.out.println(aClass);
        System.out.println(targetClass);
    }

    @Test
    public void test03(){
        for (int i = 0; i < 10;i++){
            mailThreadPool.execute(()->{
                log.info("111");
            });
        }
    }

    @Test
    public void test04(){
        List<FutureTask<String>> futureTaskList = new ArrayList<>();
        FutureTask<String> futureTask1 = new FutureTask<>(()->{
            return "result1";
        });
        FutureTask<String> futureTask2 = new FutureTask<>(()->{
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
            log.info("result:【{}】",result);
        }
    }

    @Test
    public void test05(){
        Person person = new Person(1L,"jerry",18);
        personEventService.createPerson(person);
    }


}
