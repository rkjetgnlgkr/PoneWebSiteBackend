package com.pone.website.service;

import com.pone.website.entity.User;

public interface UserProfileService {

    User getProfile(Long userId);

    void updateAvatar(Long userId, String avatarPath);
}
