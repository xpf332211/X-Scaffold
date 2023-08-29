package com.meiya.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 缓存对象 将整个枚举在类加载时加入缓存
     */
    private static final Map<Integer,ResultStatus> RESULT_STATUS_MAP =
            Stream.of(ResultStatus.values())
                    .collect(Collectors.toMap(resultStatus -> resultStatus.code,resultStatus -> resultStatus));
    /**
     * 根据状态码获得整个枚举对象
     * 优化：当枚举非常多时，for循环获取速度慢 采用缓存 以空间换时间
     * @param code 状态码
     * @return 整个枚举对象
     */
    public static ResultStatus getResultStatusEnums(Integer code){
        return RESULT_STATUS_MAP.getOrDefault(code, null);
    }

    /**
     * 根据message获取code
     * @param message 状态消息
     * @return 状态码
     */
    public static Integer getCodeByMessage(String message){
        for (ResultStatus resultStatus : ResultStatus.values()) {
            if (Objects.equals(resultStatus.message, message)){
                return resultStatus.code;
            }
        }
        return null;
    }

    /**
     * 根据code获取message
     * @param code 状态码
     * @return 状态消息
     */
    public static String getMessageByCode(Integer code){
        for (ResultStatus resultStatus : ResultStatus.values()) {
            if (resultStatus.code.equals(code)){
                return resultStatus.message;
            }
        }
        return null;
    }

}
