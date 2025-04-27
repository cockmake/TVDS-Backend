package com.tvds.newtvdsbackend.domain.vo;

import com.tvds.newtvdsbackend.domain.enums.HttpEnums;
import lombok.Data;

@Data
public class BaseResponseVO {
    private String code;
    private String message;
    private Object data;

    public BaseResponseVO(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    static public BaseResponseVO success(Object data) {
        return new BaseResponseVO(HttpEnums.SUCCESS.getCode(), HttpEnums.SUCCESS.getMessage(), data);
    }
}
