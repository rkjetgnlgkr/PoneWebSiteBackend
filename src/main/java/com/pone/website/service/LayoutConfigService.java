package com.pone.website.service;

import com.pone.website.dto.LayoutConfigDto;
import com.pone.website.entity.LayoutConfig;

public interface LayoutConfigService {

    LayoutConfig getByUserId(Long userId);

    void save(Long userId, LayoutConfigDto dto);
}
