package com.pone.website.service.impl;

import com.pone.website.dto.WorkExperienceDto;
import com.pone.website.entity.WorkExperience;
import com.pone.website.mapper.WorkExperienceMapper;
import com.pone.website.service.WorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {

    @Autowired
    private WorkExperienceMapper workExperienceMapper;

    @Override
    public List<WorkExperience> findAll(Long userId) {
        return workExperienceMapper.findAll(userId);
    }

    @Override
    public void add(WorkExperienceDto dto, Long userId) {
        WorkExperience exp = new WorkExperience();
        exp.setUserId(userId);
        exp.setCompany(dto.getCompany());
        exp.setPosition(dto.getPosition());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setIsCurrent(dto.getIsCurrent() != null && dto.getIsCurrent());
        exp.setDescription(dto.getDescription());
        exp.setSortOrder(dto.getSortOrder());
        workExperienceMapper.insert(exp);
    }

    @Override
    public void update(Long id, WorkExperienceDto dto, Long userId) {
        WorkExperience exp = workExperienceMapper.findById(id);
        if (exp == null) {
            throw new RuntimeException("工作經歷不存在");
        }
        if (!exp.getUserId().equals(userId)) {
            throw new RuntimeException("無權限修改此工作經歷");
        }
        exp.setCompany(dto.getCompany());
        exp.setPosition(dto.getPosition());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setIsCurrent(dto.getIsCurrent() != null && dto.getIsCurrent());
        exp.setDescription(dto.getDescription());
        exp.setSortOrder(dto.getSortOrder());
        workExperienceMapper.update(exp);
    }

    @Override
    public void delete(Long id, Long userId) {
        WorkExperience exp = workExperienceMapper.findById(id);
        if (exp == null) {
            throw new RuntimeException("工作經歷不存在");
        }
        if (!exp.getUserId().equals(userId)) {
            throw new RuntimeException("無權限刪除此工作經歷");
        }
        workExperienceMapper.deleteById(id);
    }
}
