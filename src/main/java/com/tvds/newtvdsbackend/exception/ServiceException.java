package com.tvds.newtvdsbackend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ServiceException extends RuntimeException {
    private final Map<String, String> errors;

    public ServiceException(Map<String, String> errors) {
        super("服务异常");
        this.errors = errors;
    }

}