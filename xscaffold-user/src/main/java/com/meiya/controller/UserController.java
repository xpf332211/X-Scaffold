package com.meiya.controller;

import com.meiya.entity.po.UserPo;
import com.meiya.entity.req.UserReq;
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
    public boolean addUser(@RequestBody UserReq userReq){
        UserPo userPo = new UserPo();
        BeanUtils.copyProperties(userReq,userPo);
        boolean save = userService.save(userPo);
        return save;
    }
}
