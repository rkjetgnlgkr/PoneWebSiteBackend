package com.pone.website.mapper;

import com.pone.website.entity.LayoutConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LayoutConfigMapper {

    LayoutConfig findByUserId(Long userId);

    void insert(LayoutConfig config);

    void update(LayoutConfig config);
}
