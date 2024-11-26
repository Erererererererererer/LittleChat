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
        userMapper.insert(user);
    }

    public void deleteById(Integer uid) {
        userMapper.deleteById(uid);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public List<User> findAll() {
        return userMapper.selectAll();
    }

    public User selectById(Integer uid) {
        return userMapper.selectById(uid);
    }
}
