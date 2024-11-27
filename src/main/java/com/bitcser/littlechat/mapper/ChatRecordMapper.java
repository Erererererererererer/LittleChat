package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.ChatRecord;

public interface ChatRecordMapper {

    // 增
    public void insert(ChatRecord record);

    // 根据ID改
    void updateById(Integer id, ChatRecord chatRecord);

    // 根据发送者和接受者ID查
    ChatRecord selectByIds(Integer senderId, Integer receiverId);
}
