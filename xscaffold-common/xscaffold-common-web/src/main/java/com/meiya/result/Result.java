package com.meiya.result;

import com.meiya.enums.ResultStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果类
 * @author xiaopf
 */

@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    private Result() {
    }

    public static <T> Result<T> build(T data, ResultStatus resultStatus) {
        Result<T> result = new Result<>();
        result.setCode(resultStatus.getCode());
        result.setMessage(resultStatus.getMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }


    public static <T> Result<T> ok() {
        return build(null, ResultStatus.SUCCESS);
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultStatus.SUCCESS);
    }

    public static <T> Result<T> error() {
        return build(null, ResultStatus.ERROR);
    }

    public static <T> Result<T> error(T data) {
        return build(data, ResultStatus.ERROR);
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }
}
