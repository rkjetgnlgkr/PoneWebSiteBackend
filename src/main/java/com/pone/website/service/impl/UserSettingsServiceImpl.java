package com.pone.website.service.impl;

import com.pone.website.dto.UserSettingsDto;
import com.pone.website.entity.UserSettings;
import com.pone.website.mapper.UserSettingsMapper;
import com.pone.website.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettings getByUserId(Long userId) {
        UserSettings settings = userSettingsMapper.findByUserId(userId);
        if (settings == null) {
            settings = new UserSettings();
            settings.setUserId(userId);
            settings.setThemeStyle("default");
        }
        return settings;
    }

    @Override
    public void save(Long userId, UserSettingsDto dto) {
        UserSettings existing = userSettingsMapper.findByUserId(userId);
        if (existing == null) {
            UserSettings settings = new UserSettings();
            settings.setUserId(userId);
            settings.setThemeStyle(dto.getThemeStyle());
            userSettingsMapper.insert(settings);
        } else {
            existing.setThemeStyle(dto.getThemeStyle());
            userSettingsMapper.update(existing);
        }
    }
}
