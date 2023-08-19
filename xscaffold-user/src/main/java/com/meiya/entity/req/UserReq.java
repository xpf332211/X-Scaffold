package com.meiya.entity.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaopf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReq {
    /**
     * 用户名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;
}
