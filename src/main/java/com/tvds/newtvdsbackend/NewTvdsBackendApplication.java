package com.tvds.newtvdsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.tvds.newtvdsbackend.mapper")
@EnableTransactionManagement
public class NewTvdsBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewTvdsBackendApplication.class, args);
    }
}