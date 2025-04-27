package com.tvds.newtvdsbackend.utils;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct; // 引入 PostConstruct

@Component
@ConfigurationProperties(prefix = "cluster.snowflake")
@Data
public class SnowflakeUtil {

    // 实例变量，用于接收 Spring 注入的配置值
    private Long workerId;
    private Long datacenterId;

    private static SnowflakeGenerator snowflakeGenerator;

    // 使用 @PostConstruct 在依赖注入完成后初始化静态变量
    @PostConstruct
    public void init() {
        snowflakeGenerator = new SnowflakeGenerator(this.workerId, this.datacenterId);
    }

    // 静态方法，可以直接通过类名调用
    public static Long getNext() {
        return snowflakeGenerator.next();
    }
}