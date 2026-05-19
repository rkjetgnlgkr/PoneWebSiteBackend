package com.pone.website.mapper;

import com.pone.website.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    User findByGoogleId(String googleId);

    User findByLineId(String lineId);

    void insertUser(User user);

    void updateGoogleId(@Param("id") Long id, @Param("googleId") String googleId);

    void updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);

    User findById(Long id);

    List<User> findAll();
}
