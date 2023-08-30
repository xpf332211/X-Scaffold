package com.meiya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiya.entity.dto.UserDto;
import com.meiya.entity.po.UserPo;
import com.meiya.mapper.UserMapper;
import com.meiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author xiaopf
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {


    @Override
    @Async("mdcThreadPool")
    public CompletableFuture<String> asyncRun() {
        try {
            //执行耗时操作
            Thread.sleep(2000);
            log.info("子线程独立任务执行完毕");
        } catch (InterruptedException e) {
            log.error("发生异常",e);
        }
        return CompletableFuture.completedFuture("async run over..");
    }

    @Override
    public String syncRun() {
        try {
            //执行耗时操作
            Thread.sleep(2000);
            log.info("任务执行完毕");
        } catch (InterruptedException e) {
            log.error("发生异常",e);
        }
        return "sync run over..";
    }
}
