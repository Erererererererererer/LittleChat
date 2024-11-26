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

    // 查所有
    List<User> selectAll();

    // 根据ID查
    User selectById(Integer id);
}
