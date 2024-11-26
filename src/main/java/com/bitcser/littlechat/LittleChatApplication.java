package com.bitcser.littlechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bitcser.littlechat.mapper")
public class LittleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(LittleChatApplication.class, args);
    }

}
