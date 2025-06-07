package com.tvds.newtvdsbackend.exception;

import com.tvds.newtvdsbackend.domain.enums.HttpEnums;
import com.tvds.newtvdsbackend.domain.vo.BaseResponseVO;
import lombok.Data;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Data
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponseVO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new BaseResponseVO(HttpEnums.BAD_REQUEST.getCode(), HttpEnums.BAD_REQUEST.getMessage(), errors);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponseVO handleServiceException(ServiceException ex) {
        return new BaseResponseVO(HttpEnums.INTERNAL_SERVER_ERROR.getCode(), HttpEnums.INTERNAL_SERVER_ERROR.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponseVO handleLoginException(LoginException ex) {
        return new BaseResponseVO(HttpEnums.UNAUTHORIZED.getCode(), HttpEnums.UNAUTHORIZED.getMessage(), ex.getErrors());
    }

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_PRE_UPLOAD_SIZE;
    @Value("${spring.servlet.multipart.max-request-size}")
    private String MAX_REQUEST_SIZE;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponseVO handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.printStackTrace();
        System.err.println("Unhandled exception occurred: " + ex);
        errors.put("1", "文件大小超过限制");
        errors.put("2", "单文件最大为" + MAX_PRE_UPLOAD_SIZE);
        errors.put("3", "总文件最大为" + MAX_REQUEST_SIZE);
        return new BaseResponseVO(HttpEnums.BAD_REQUEST.getCode(), HttpEnums.BAD_REQUEST.getMessage(), errors);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponseVO handleDataAccessException(DataAccessException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("1", "数据库操作异常");
        ex.printStackTrace();
        System.err.println("Unhandled exception occurred: " + ex);
        return new BaseResponseVO(HttpEnums.INTERNAL_SERVER_ERROR.getCode(), HttpEnums.INTERNAL_SERVER_ERROR.getMessage(), errors);
    }

    // 可以添加一个通用的异常处理器作为最后的保障
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponseVO handleGeneralException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "服务器内部错误");
        // 记录日志会更有帮助
        // log.error("Unhandled exception occurred", ex);
        ex.printStackTrace();
        System.err.println("Unhandled exception occurred: " + ex);
        return new BaseResponseVO(HttpEnums.INTERNAL_SERVER_ERROR.getCode(), HttpEnums.INTERNAL_SERVER_ERROR.getMessage(), errors);
    }
}
