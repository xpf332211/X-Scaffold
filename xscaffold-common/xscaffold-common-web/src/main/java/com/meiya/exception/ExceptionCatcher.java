package com.meiya.exception;

import com.meiya.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xiaopf
 */
@RestControllerAdvice
public class ExceptionCatcher<T> {

    @ExceptionHandler({RuntimeException.class})
    public Result<T> runTimeException(RuntimeException e){
        e.printStackTrace();
        return Result.error();
    }
}
