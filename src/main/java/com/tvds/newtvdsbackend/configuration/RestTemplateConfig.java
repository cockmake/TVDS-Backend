package com.tvds.newtvdsbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 你可以在这里添加自定义配置，例如消息转换器、错误处理器等
        return new RestTemplate();
    }
}