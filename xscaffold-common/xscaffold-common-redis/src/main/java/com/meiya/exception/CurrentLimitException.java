package com.meiya.exception;

/**
 * @author xiaopf
 */
public class CurrentLimitException extends Exception{
    public CurrentLimitException(String message){
        super(message);
    }
}
