package com.meiya.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiaopf
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultStatus {
    SUCCESS(200,"成功"),
    ERROR(500,"失败");
    private Integer code;
    private String message;

}
