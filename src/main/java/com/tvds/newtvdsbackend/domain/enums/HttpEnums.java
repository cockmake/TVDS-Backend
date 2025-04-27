package com.tvds.newtvdsbackend.domain.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum HttpEnums {
    SUCCESS("200", "请求成功"),
    ERROR("500", "请求失败"),
    NOT_FOUND("404", "请求地址不存在"),
    UNAUTHORIZED("401", "未授权"),
    FORBIDDEN("403", "禁止访问"),
    BAD_REQUEST("400", "错误的请求参数"),
    INTERNAL_SERVER_ERROR("500", "服务器错误");

    private final String code;
    private final String message;

    HttpEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
