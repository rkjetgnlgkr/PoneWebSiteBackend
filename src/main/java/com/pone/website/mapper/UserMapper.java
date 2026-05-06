package com.pone.website.mapper;

import com.pone.website.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    void insertUser(User user);

    List<User> findAll();
}
