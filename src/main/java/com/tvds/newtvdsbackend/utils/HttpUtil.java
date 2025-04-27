package com.tvds.newtvdsbackend.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "http")
public class HttpUtil {
    private String fastapiUrl;
    public static String staticFastapiUrl;

    @PostConstruct
    public void init() {
        // 这里可以进行一些初始化操作
        staticFastapiUrl = fastapiUrl;
    }
}
