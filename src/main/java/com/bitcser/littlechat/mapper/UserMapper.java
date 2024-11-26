package com.bitcser.littlechat.mapper;

import com.bitcser.littlechat.entity.User;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface UserMapper {

    void insert(User user);

    @Delete("DELETE FROM `user` WHERE uid = #{uid}")
    void deleteById(Integer uid);

    void updateById(User user);

    List<User> selectAll();

    User selectById(Integer uid);
}
