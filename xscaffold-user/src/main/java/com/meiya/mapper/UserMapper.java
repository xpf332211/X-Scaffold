package com.meiya.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiya.entity.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaopf
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

}
