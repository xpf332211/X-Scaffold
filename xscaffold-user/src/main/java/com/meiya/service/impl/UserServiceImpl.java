package com.meiya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiya.entity.po.UserPo;
import com.meiya.mapper.UserMapper;
import com.meiya.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author xiaopf
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {
}
