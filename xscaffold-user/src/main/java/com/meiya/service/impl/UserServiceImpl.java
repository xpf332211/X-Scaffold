package com.meiya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiya.entity.dto.UserDto;
import com.meiya.entity.po.UserPo;
import com.meiya.mapper.UserMapper;
import com.meiya.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaopf
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {


    @Override
    public void run() {

    }
}
