package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.User;

import java.util.List;

public interface UserMapper {

    // 增
    void insert(User user);

    // 根据ID删
    void deleteById(Integer id);

    // 根据ID改
    void updateById(User user);

    // 根据ID改online字段
    void updateOnlineById(Integer id, Integer online);

    // 查所有
    List<User> selectAll();

    // 根据ID查
    User selectById(Integer id);

    // 根据username查
    User selectByUsername(String username);

    // 根据phone查
    User selectByPhone(String phone);
}
