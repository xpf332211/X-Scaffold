package com.meiya;

import com.meiya.entity.po.UserPo;
import com.meiya.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = UserApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserTest {

    @Resource
    private UserService userService;

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

}
