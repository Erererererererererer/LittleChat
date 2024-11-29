package com.bitcser.littlechat.service;

import com.bitcser.littlechat.entity.ChatRecord;
import com.bitcser.littlechat.entity.User;
import com.bitcser.littlechat.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    // 更新用户当前聊天框，0表示不在任意一个聊天框
    public void updateView(Integer id, Integer allowedSenderId) {
        userMapper.updateOnlineById(id, allowedSenderId);
    }

    // 查询所有用户
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    // 根据ID查询用户
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    // 根据username查询用户
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    // 根据phone查询用户
    public User findByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    // 用户登录验证
    public Boolean checkLogin(Integer userId, String password) {
        User user = userMapper.selectById(userId);
        return user.getPassword().equals(password);
    }

    // 查询用户是否在线
    public Boolean online(Integer id) {
        Integer online = userMapper.selectById(id).getOnline();
        return online == 1;
    }

    public Boolean view(Integer id, Integer allowedSenderId) {
        Integer online = userMapper.selectById(id).getOnline();
        return Objects.equals(online, allowedSenderId);
    }
}
