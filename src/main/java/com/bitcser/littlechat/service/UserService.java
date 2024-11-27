package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.User;
import com.bitcser.littlechat.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void add(User user) {
        user.setOnline(0);
        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    // 更新用户在线状态
    public void updateOnline(Integer id, boolean online) {
        if (online) {
            userMapper.updateOnlineById(id, 1);
        } else {
            userMapper.updateOnlineById(id, 0);
        }
    }

    public List<User> findAll() {
        return userMapper.selectAll();
    }

    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    // 查询用户是否在线
    public Boolean online(Integer id) {
        Integer online = userMapper.selectById(id).getOnline();
        return online == 1;
    }
}
