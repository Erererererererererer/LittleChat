package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.ChatRecord;

import java.util.List;

public interface ChatRecordMapper {

    // 增
    void insert(ChatRecord record);

    // 根据ID改
    void updateById(ChatRecord chatRecord);

    // 根据发送者ID查所有
    List<ChatRecord> selectAll(Integer senderId);

    // 根据发送者和接受者ID查
    ChatRecord selectByIds(Integer senderId, Integer receiverId);
}
