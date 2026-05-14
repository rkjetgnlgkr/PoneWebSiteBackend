package com.pone.website.mapper;

import com.pone.website.entity.UserSettings;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSettingsMapper {

    UserSettings findByUserId(Long userId);

    void insert(UserSettings settings);

    void update(UserSettings settings);
}
