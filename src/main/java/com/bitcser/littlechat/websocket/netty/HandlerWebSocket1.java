package com.bitcser.littlechat.websocket.netty;

import com.bitcser.littlechat.common.Receive;
import com.bitcser.littlechat.common.Result;
import com.bitcser.littlechat.common.Result0;
import com.bitcser.littlechat.service.ChatRecordSevice;
import com.bitcser.littlechat.service.MessageService;
import com.bitcser.littlechat.service.UserService;
import com.bitcser.littlechat.websocket.ChannelContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// handler（半新版，对应逻辑为无条件建立socket连接，登录一起放入后续操作；并且全程只用socket通信，而不使用http）
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket1 extends SimpleChannelInboundHandler<TextWebSocketFrame> {

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

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket1.class);

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
        String text = textWebSocketFrame.text(); // 消息内容
        logger.info("收到消息：{}",  text);
        //消息格式：user/login/id=1&password=111111
        //  chat/chat/receiverId=2&message=ooohhhhhhh

        String userId = null;
        receive.splitText(text);
        if (receive.getModule().equals("user") && receive.getService().equals("login")) {
            userId = receive.getParamsMap().get("id");
            // 绑定channel和用户ID
            channelContextUtils.addContext(userId, ctx.channel());
        } else {
            // 获取用户ID
            Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
            userId = attribute.get();
        }

        // 调用Service进行处理
        Result0 result = receive.runService();
        result.setSenderId(userId);

        // 发送反馈消息
        // 将Result对象序列化为JSON字符串
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(result);
        channelContextUtils.sendMessage(channel, message);
        // channelContextUtils.sendMessage(userId, message);
        // 发送聊天消息（如果用户在线，则直接发送；不在线则存数据库）
        String receiverId = result.getReceiverId();
        if (receiverId != null) {
            if (userService.online(Integer.valueOf(receiverId))) {
                // 发送，存message
                channelContextUtils.sendMessage(receiverId, message);
                messageService.add(Integer.valueOf(userId), Integer.valueOf(receiverId), (String)result.getData());
            } else {
                // 存message，更新record
                messageService.add(Integer.valueOf(userId), Integer.valueOf(receiverId), (String)result.getData());
                chatRecordSevice.update(Integer.valueOf(userId), Integer.valueOf(receiverId), (String)result.getData());
            }
        }
    }

}
