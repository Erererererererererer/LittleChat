package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.mapper.ChatRecordMapper;

import com.bitcser.littlechat.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ChatRecordSevice {

    @Resource
    ChatRecordMapper chatRecordMapper;
    @Resource
    MessageMapper messageMapper;

    // 新增会话记录
    public void add(Integer senderId, Integer receiverId, String lastMessage) {
        // 新增时未读消息数自动设置为1
        ChatRecord chatRecord = new ChatRecord(null, senderId, receiverId, lastMessage,
                new Timestamp(System.currentTimeMillis()), 1, 0);
        chatRecordMapper.insert(chatRecord);
    }

    // 更新会话记录
    public void update(Integer senderId, Integer receiverId, String lastMessage, Integer countAdd) {
        // 先查询会话是否存在
        ChatRecord chatRecord = findByIds(senderId, receiverId);
        if (chatRecord == null) {
            // 如果会话不存在，直接新增
            add(senderId, receiverId, lastMessage);
        } else {
            // 如果会话存在，更新最后一条记录、最后一条的时间、未读消息数
            chatRecord.setLastMessage(lastMessage);
            chatRecord.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            chatRecord.setUnreadCount(chatRecord.getUnreadCount() + countAdd);
            chatRecordMapper.updateById(chatRecord);
        }
    }

    // 更新为全部已读
    public void updateUnread(Integer senderId, Integer receiverId) {
        chatRecordMapper.updateUnreadCount(senderId, receiverId);
    }

    // 查询所有某人收到的所有会话
    public List<ChatRecord> findAllByReceiverId(Integer receiverId) {
        return chatRecordMapper.selectAllByReceiverId(receiverId);
    }

    // 查询所有某人发出的所有会话
    public List<ChatRecord> findAllBySenderId(Integer senderId) {
        return chatRecordMapper.selectAllBySenderId(senderId);
    }

    // 查询会话记录
    public ChatRecord findByIds(Integer senderId, Integer receiverId) {
        return chatRecordMapper.selectByIds(senderId, receiverId);
    }

}
