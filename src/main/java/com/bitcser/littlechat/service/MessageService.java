package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.Message;
import com.bitcser.littlechat.mapper.MessageMapper;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    public void add(Integer senderId, Integer receiverId, String content) {
        Message message = new Message(senderId, receiverId, content, 1, 1, new Timestamp(System.currentTimeMillis()));
        messageMapper.insert(message);
    }

}
