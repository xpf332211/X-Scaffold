package com.meiya.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiya.entity.dto.UserDto;
import com.meiya.entity.po.UserPo;

import java.util.concurrent.CompletableFuture;

/**
 * @author xiaopf
 */
public interface UserService extends IService<UserPo> {
    /**
     * 异步执行 不同线程
     * @return CompletableFuture对象
     */
    CompletableFuture<String> asyncRun();

    /**
     * 同步执行 相同线程
     * @return 字符串对象
     */
    String syncRun();
}
