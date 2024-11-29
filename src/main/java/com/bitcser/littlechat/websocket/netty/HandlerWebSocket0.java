package com.bitcser.littlechat.websocket.netty;

import com.bitcser.littlechat.service.ChatRecordSevice;
import com.bitcser.littlechat.service.MessageService;
import com.bitcser.littlechat.service.UserService;
import com.bitcser.littlechat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// handler（原版，对应逻辑为登录时同时建立socket连接）
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket0 extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private ChatRecordSevice chatRecordSevice;

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket0.class);

    //  通道就绪后调用，一般用来做初始化
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开...");
        // 获取用户ID
        Channel channel = ctx.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        // 修改为离线
        userService.updateOnline(Integer.valueOf(userId), false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();

        // 获取用户ID
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        logger.info("收到{}的消息：{}", userId, textWebSocketFrame.text());

        // 解析消息并发送
        String receiverId = textWebSocketFrame.text().split("/")[0];
        String message = textWebSocketFrame.text().split("/")[1];
        // 如果用户在线，则直接发送
        if (userService.online(Integer.valueOf(receiverId))) {
            //channelContextUtils.sendMessage(userId, receiverId, message);
        } else {
            // 存message，更新record
            messageService.add(Integer.valueOf(userId), Integer.valueOf(receiverId), message);
            chatRecordSevice.update(Integer.valueOf(userId), Integer.valueOf(receiverId), message, 1);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = complete.requestUri();
            // 验证用户ID
            String userId = getUserId(url);
            if (userId == null) {
                ctx.channel().close();
            }

            // 绑定channel和用户ID
            channelContextUtils.addContext(userId, ctx.channel());
            // 修改为在线
            userService.updateOnline(Integer.valueOf(userId), true);
        }
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
