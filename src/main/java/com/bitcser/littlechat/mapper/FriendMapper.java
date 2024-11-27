package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.Friend;

import java.util.List;

public interface FriendMapper {

    // 发送好友请求状态
    void insert(Friend friend);

    // 删除好友
    void deleteById(Integer userId, Integer friendId);

    // 接受好友请求
    void updateById(Friend friend);

    // 查询用户i的所有好友
    List<Friend> selectAll(Integer userId);

}
