package com.meiya.exception;

/**
 * @author xiaopf
 */
public class DistributedLockException extends RuntimeException{
    public DistributedLockException(String message){
        super(message);
    }
}
