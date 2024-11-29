package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.Message;
import com.bitcser.littlechat.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageService {
    @Resource
    private MessageMapper messageMapper;

    public void add(Message message) {
        messageMapper.insert(message);
    }

    public void add(Integer senderId, Integer receiverId, String content) {
        Message message = new Message(null, senderId, receiverId, content, 1, 1, new Timestamp(System.currentTimeMillis()));
        messageMapper.insert(message);
    }

    public void deleteById(Integer senderId, Integer receiverId) {
        messageMapper.deleteById(senderId, receiverId);
    }

    public void update(Message message) {
        messageMapper.updateById(message);
    }

    public List<Message> findAll(Integer userId, Integer friendId) {
        return messageMapper.selectAll(userId, friendId);
    }
}