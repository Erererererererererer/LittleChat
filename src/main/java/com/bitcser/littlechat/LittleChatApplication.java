package com.bitcser.littlechat;

import com.bitcser.littlechat.websocket.netty.NettyWebSocketStarter;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.bitcser.littlechat"})
@MapperScan("com.bitcser.littlechat.mapper")
@EnableAsync // 允许异步
public class LittleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(LittleChatApplication.class, args);
    }

}
