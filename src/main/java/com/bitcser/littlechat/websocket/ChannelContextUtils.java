package com.bitcser.littlechat.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelContextUtils {

    // 用户与通道的对应表（全局，线程安全）
    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();

    public void addContext(String userId, Channel channel) {
        // 绑定channel和用户ID
        String channelId = channel.id().toString();
        AttributeKey attributeKey = null;
        if (AttributeKey.exists(channelId)) {
            attributeKey = AttributeKey.valueOf(channelId);
        } else {
            attributeKey = AttributeKey.newInstance(channelId);
        }
        channel.attr(attributeKey).set(userId);

        USER_CONTEXT_MAP.put(userId, channel);
    }

    // 配合HandlerWebSocket0使用
//    public void sendMessage(String senderId, String receiverId, String message) {
//        Channel channel = USER_CONTEXT_MAP.get(receiverId);
//        channel.writeAndFlush(new TextWebSocketFrame(senderId + "/" + message));
//    }

    public void sendMessage(String receiverId, String message) {
        Channel channel = USER_CONTEXT_MAP.get(receiverId);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public void sendMessage(Channel channel, String message) {
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

}
