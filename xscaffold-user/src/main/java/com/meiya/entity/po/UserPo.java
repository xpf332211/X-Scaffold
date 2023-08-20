package com.meiya.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.meiya.entity.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * @author xiaopf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class UserPo extends BaseEntity {


    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;


}
