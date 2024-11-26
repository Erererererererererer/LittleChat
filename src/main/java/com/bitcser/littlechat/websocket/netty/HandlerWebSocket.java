package com.bitcser.littlechat.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);

    //  通道就绪后调用，一般用来做初始化
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        logger.info("收到消息{}", textWebSocketFrame.text());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = complete.requestUri();
            // 验证用户ID
            String id = getUserId(url);
            if (id == null) {
                ctx.channel().close();
            }
            logger.info("url: {}", url);
            logger.info("id: {}", id);
        }
        super.userEventTriggered(ctx, evt);
    }

    // 解析连接时的用户ID
    private String getUserId(String url) {
        if (url.isEmpty() || !url.contains("?")) {
            // 如果url为空或没带参数
            return null;
        }
        String[] params = url.split("\\?");
        if (params.length != 2) {
            return null;
        }
        String[] paramList = params[1].split("=");
        if (paramList.length != 2 || !paramList[0].equals("id")) {
            return null;
        }
        return paramList[1];
    }
}
