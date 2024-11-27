package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.Message;

import java.util.List;

public interface MessageMapper {

    // 发送消息
    void insert(Message message);

    // 删除聊天记录
    void deleteById(Integer senderId, Integer receiverId);

    // 撤回消息（根据消息ID）
    void updateById(Message message);

    // 查询用户i及其好友j的全部聊天记录
    List<Message> selectAll(Integer senderId, Integer receiverId);
}
