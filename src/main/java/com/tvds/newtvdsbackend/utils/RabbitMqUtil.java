package com.tvds.newtvdsbackend.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties(prefix = "common.rabbitmq")
public class RabbitMqUtil {
    // 这里只用producer端的配置
    // 因为consumer端的通过注解已经快速生成了
    // 静态字段用于存储配置值
    public static String COMPONENT_LOCATION_EXCHANGE_NAME;
    public static String PRODUCER_COMPONENT_LOCATION_ROUTING_KEY;
    public static String PRODUCER_COMPONENT_LOCATION_QUEUE_NAME;

    // 实例字段用于接收注入的值
    private String componentLocationExchangeName;
    private String producerComponentLocationRoutingKey;
    private String producerComponentLocationQueueName;

    // 初始化静态字段
    @PostConstruct
    public void init() {
        COMPONENT_LOCATION_EXCHANGE_NAME = componentLocationExchangeName;
        PRODUCER_COMPONENT_LOCATION_ROUTING_KEY = producerComponentLocationRoutingKey;
        PRODUCER_COMPONENT_LOCATION_QUEUE_NAME = producerComponentLocationQueueName;
    }
}
