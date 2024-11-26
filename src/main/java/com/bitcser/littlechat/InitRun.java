package com.bitcser.littlechat;

import com.bitcser.littlechat.websocket.netty.NettyWebSocketStarter;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component("initRun")
public class InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private NettyWebSocketStarter nettyWebSocketStarter;

    @Override
    public void run(ApplicationArguments args) {
        try {
            new Thread(nettyWebSocketStarter).start();
            logger.info("服务启动成功");
        } catch (Exception e) {
            logger.error("服务启动失败", e);
        }
    }
}
