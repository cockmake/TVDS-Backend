package com.tvds.newtvdsbackend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class LoginException extends RuntimeException {
    private final Map<String, String> errors;

    public LoginException(Map<String, String> errors) {
        super("登录异常"); // 您可以根据需要修改默认的异常消息
        this.errors = errors;
    }

    public LoginException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}