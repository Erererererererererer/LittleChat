package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.Message;

public interface MessageMapper {

    // 增
    public void insert(Message message);

    // 查
    public Message selectById(Integer id);
}
