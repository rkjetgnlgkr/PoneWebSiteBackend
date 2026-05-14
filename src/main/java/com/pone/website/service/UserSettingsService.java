package com.pone.website.service;

import com.pone.website.dto.UserSettingsDto;
import com.pone.website.entity.UserSettings;

public interface UserSettingsService {

    UserSettings getByUserId(Long userId);

    void save(Long userId, UserSettingsDto dto);
}
