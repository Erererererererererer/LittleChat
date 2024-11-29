package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.Friend;
import com.bitcser.littlechat.mapper.FriendMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    @Resource
    private FriendMapper friendMapper;

    public void add(Friend friend) {
        friendMapper.insert(friend);
    }

    // 新增一个好友关系
    public void add(Integer userId, Integer friendId, Integer status) {
        friendMapper.insert(new Friend(null, userId, friendId, status));
    }

    public void deleteById(Integer userId, Integer friendId) {
        // 双向删除
        friendMapper.deleteById(userId, friendId);
        friendMapper.deleteById(friendId, userId);
    }

    public void update(Friend friend) {
        friendMapper.updateById(friend);
    }

    // 修改状态为“已同意”
    public void updateStatus(Integer userId, Integer friendId) {
        friendMapper.updateById(new Friend(null, userId, friendId, 1));
    }

    public List<Friend> findAll(Integer userId) {
        return friendMapper.selectAll(userId);
    }

    public List<Friend> findAllByFriendId(Integer userId) {
        return friendMapper.selectAllByFriendId(userId);
    }
}
