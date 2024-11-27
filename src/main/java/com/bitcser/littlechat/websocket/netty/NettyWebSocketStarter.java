package com.bitcser.littlechat.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// netty启动类
@Component
public class NettyWebSocketStarter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketStarter.class);

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 设置几个重要的处理器
                            // 对http协议的支持，使用http的编码器、解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 聚合解码，httpRequest/httpContent/lastHttpContent到fullHttpRequest，保证接收的http请求的完整性
                            pipeline.addLast(new HttpObjectAggregator(64*1024));
                            // 心跳 long readerIdleTime读超时, long writerIdleTime写超时, long allIdleTime所有类型的超时时间, TimeUnit unit
                            pipeline.addLast(new IdleStateHandler(600, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            // 将http协议升级为ws协议，对websocket支持，url: ws://localhost:port/ws
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536, false, true));
                            pipeline.addLast(handlerWebSocket);
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(8091).sync();
            logger.info("netty启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("启动netty失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

    }
}
