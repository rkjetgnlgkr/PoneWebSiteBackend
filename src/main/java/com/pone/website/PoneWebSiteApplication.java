package com.pone.website;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pone.website.mapper")
public class PoneWebSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoneWebSiteApplication.class, args);
    }
}
