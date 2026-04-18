package com.pone.website.mapper;

import com.pone.website.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    void insertUser(User user);
}
