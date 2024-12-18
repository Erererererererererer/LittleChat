package com.bitcser.littlechat.websocket.netty;
import com.bitcser.littlechat.common.Receive;
import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.service.ChatRecordSevice;
import com.bitcser.littlechat.service.MessageService;
import com.bitcser.littlechat.websocket.ChannelContextUtils;
import com.bitcser.littlechat.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.stream.ImageOutputStream;
import java.util.Arrays;

// handler（新版，对应逻辑为无条件建立socket连接，登录一起放入后续操作；并且聊天用socket通信，其余时候用http请求）
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Resource
    private Receive receive;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private ChatRecordSevice chatRecordSevice;

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);

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
        //userService.updateOnline(Integer.valueOf(userId), false);
        userService.updateView(Integer.valueOf(userId), 0);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        String text = textWebSocketFrame.text(); // 消息内容
        logger.info("收到消息：{}",  text);

        if (!text.contains("/")) {
            // 无法解析，格式错误
            channelContextUtils.sendMessage(channel, "error");
            return;
        }

        String method = text.split("/")[0];
        if (method.equals("login")) {
            if (text.split("/").length != 3) {
                // 无法解析，格式错误
                channelContextUtils.sendMessage(channel, "error");
                return;
            }
            String userId = text.split("/")[1];
            String allowedSenderId = text.split("/")[2];
            // 修改在线可见状态
            userService.updateView(Integer.valueOf(userId), Integer.valueOf(allowedSenderId));
            // 绑定channel和用户ID
            channelContextUtils.addContext(userId, ctx.channel());
        } else if (method.equals("friend")) {
            String receiverId = text.split("/")[1];

            // 获取用户ID
            Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
            String userId = attribute.get();

        } else if (method.equals("chat")) {
            String receiverId = text.split("/")[1];
            String message = text.split("/")[2];

            // 获取用户ID
            Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
            String userId = attribute.get();

            // 发送聊天消息（如果用户在线，则直接发送；不在线则存数据库）
            //if (userService.online(Integer.valueOf(receiverId))) {
            if (userService.view(Integer.valueOf(receiverId), Integer.valueOf(userId))) {
                // 发送，存message，更新record（但是不增加未读消息数）
                channelContextUtils.sendMessage(receiverId, "chat/" + userId + "/" + receiverId + "/" + message);
                messageService.add(Integer.valueOf(userId), Integer.valueOf(receiverId), message);
                chatRecordSevice.update(Integer.valueOf(userId), Integer.valueOf(receiverId), message, 0);
            } else {
                // 存message，更新record
                messageService.add(Integer.valueOf(userId), Integer.valueOf(receiverId), message);
                chatRecordSevice.update(Integer.valueOf(userId), Integer.valueOf(receiverId), message, 1);
            }
        }
    }

}
