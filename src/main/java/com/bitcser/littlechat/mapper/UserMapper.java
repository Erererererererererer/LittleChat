package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.User;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface UserMapper {

    void insert(User user);

    void deleteById(Integer id);

    void updateById(User user);

    List<User> selectAll();

    User selectById(Integer id);
}
