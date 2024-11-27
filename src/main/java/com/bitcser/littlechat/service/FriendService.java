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

    public void deleteById(Integer userId, Integer friendId) {
        friendMapper.deleteById(userId, friendId);
    }

    public void update(Friend friend) {
        friendMapper.updateById(friend);
    }

    public List<Friend> findAll(Integer userId) {
        return friendMapper.selectAll(userId);
    }

}
