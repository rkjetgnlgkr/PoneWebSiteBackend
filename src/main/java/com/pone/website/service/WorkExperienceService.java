package com.pone.website.service;

import com.pone.website.dto.WorkExperienceDto;
import com.pone.website.entity.WorkExperience;

import java.util.List;

public interface WorkExperienceService {
    List<WorkExperience> findAll(Long userId);
    void add(WorkExperienceDto dto, Long userId);
    void update(Long id, WorkExperienceDto dto, Long userId);
    void delete(Long id, Long userId);
}
