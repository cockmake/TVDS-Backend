package com.tvds.newtvdsbackend.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "common")
@Data
public class CommonUtil {
    private List<String> imageType;


    private static List<String> staticImageType;
    @PostConstruct
    public void init() {
        // 这里可以进行一些初始化操作
        // 比如将 imageType 和 staticImageType 转换为小写
        staticImageType = imageType;
    }
    static public boolean imageTypeCheck(String extension) {
        return staticImageType.contains(extension);
    }
    static public List<String> imageTypeList() {
        return staticImageType;
    }
}
