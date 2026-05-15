package com.pone.website.service.impl;

import com.pone.website.entity.User;
import com.pone.website.mapper.UserMapper;
import com.pone.website.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getProfile(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public void updateAvatar(Long userId, String avatarPath) {
        userMapper.updateAvatar(userId, avatarPath);
    }
}
