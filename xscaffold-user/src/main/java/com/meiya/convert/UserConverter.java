package com.meiya.convert;

import com.meiya.entity.po.UserPo;
import com.meiya.entity.req.UserReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaopf
 */
@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * 将req对象拷贝成po对象
     * @param userReq user请求实体
     * @return user数据库实体
     */
    @Mapping(source = "userReq.age",target = "age")
    UserPo convertReqToPo(UserReq userReq);
}
