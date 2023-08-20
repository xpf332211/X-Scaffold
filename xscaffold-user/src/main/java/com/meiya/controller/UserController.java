package com.meiya.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiya.convert.UserConverter;
import com.meiya.entity.po.UserPo;
import com.meiya.entity.req.UserReq;
import com.meiya.entity.PageResult;
import com.meiya.result.Result;
import com.meiya.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaopf
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String getUserId(@PathVariable Long id){
        return "my id is " + id;
    }

    @PostMapping
    public Result<UserPo> addUser(@RequestBody UserReq userReq){
        UserPo userPo = UserConverter.INSTANCE.convertReqToPo(userReq);
        userService.save(userPo);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<UserPo> deleteUser(@PathVariable Long id){
        userService.removeById(id);
        return Result.ok();
    }

    @GetMapping
    public PageResult<UserPo> pageUser(@RequestBody UserReq userReq){
        IPage<UserPo> userPoPage = new Page<>(userReq.getPageIndex(),userReq.getPageSize());
        IPage<UserPo> page = userService.page(userPoPage);
        PageResult<UserPo> result = new PageResult<>();
        result.loadPageData(page);
        return result;
    }
}
