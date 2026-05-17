package com.pone.website.service;

import com.pone.website.dto.SkillDto;
import com.pone.website.entity.Skill;

import java.util.List;

public interface SkillService {
    List<Skill> findAll(Long userId);
    void add(SkillDto dto, Long userId);
    void update(Long id, SkillDto dto, Long userId);
    void delete(Long id, Long userId);
}
