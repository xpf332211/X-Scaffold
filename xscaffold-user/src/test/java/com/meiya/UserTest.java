package com.meiya;

import com.meiya.entity.po.UserPo;
import com.meiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest(classes = UserApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class UserTest {

    @Resource
    private UserService userService;

    @Resource(name = "mailThreadPool")
    private ThreadPoolExecutor mailThreadPool;

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


}
