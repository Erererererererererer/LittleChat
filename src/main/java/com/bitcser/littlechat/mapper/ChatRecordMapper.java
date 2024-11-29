package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.ChatRecord;

import java.util.List;

public interface ChatRecordMapper {

    // 增
    void insert(ChatRecord record);

    // 根据ID改
    void updateById(ChatRecord chatRecord);

    // 修改未读消息为0条
    void updateUnreadCount(Integer senderId, Integer receiverId);

    // 根据接收者ID查所有
    List<ChatRecord> selectAllByReceiverId(Integer receiverId);

    // 根据发送者ID查所有
    List<ChatRecord> selectAllBySenderId(Integer SenderId);

    // 根据发送者和接受者ID查
    ChatRecord selectByIds(Integer senderId, Integer receiverId);
}
