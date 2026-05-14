package com.pone.website.service.impl;

import com.pone.website.dto.LayoutConfigDto;
import com.pone.website.entity.LayoutConfig;
import com.pone.website.mapper.LayoutConfigMapper;
import com.pone.website.service.LayoutConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayoutConfigServiceImpl implements LayoutConfigService {

    @Autowired
    private LayoutConfigMapper layoutConfigMapper;

    @Override
    public LayoutConfig getByUserId(Long userId) {
        LayoutConfig config = layoutConfigMapper.findByUserId(userId);
        if (config == null) {
            config = new LayoutConfig();
            config.setUserId(userId);
            config.setThemeStyle("dark_star");
        }
        return config;
    }

    @Override
    public void save(Long userId, LayoutConfigDto dto) {
        LayoutConfig existing = layoutConfigMapper.findByUserId(userId);
        if (existing == null) {
            LayoutConfig config = new LayoutConfig();
            config.setUserId(userId);
            config.setThemeStyle(dto.getThemeStyle());
            layoutConfigMapper.insert(config);
        } else {
            existing.setThemeStyle(dto.getThemeStyle());
            layoutConfigMapper.update(existing);
        }
    }
}
