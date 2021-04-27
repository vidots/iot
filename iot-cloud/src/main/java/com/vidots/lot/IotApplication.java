package com.vidots.lot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.vidots.lot.repo")
public class IotApplication {
    public static void main(String[] args) {
        SpringApplication.run(IotApplication.class, args);
    }
}
