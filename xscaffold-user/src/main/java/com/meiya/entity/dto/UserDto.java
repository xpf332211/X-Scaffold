package com.meiya.entity.dto;

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
public class UserDto {
    /**
     * 用户名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;
}
