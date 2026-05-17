package com.pone.website.service.impl;

import com.pone.website.dto.SkillDto;
import com.pone.website.entity.Skill;
import com.pone.website.mapper.SkillMapper;
import com.pone.website.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillMapper skillMapper;

    @Override
    public List<Skill> findAll(Long userId) {
        return skillMapper.findAll(userId);
    }

    @Override
    public void add(SkillDto dto, Long userId) {
        Skill skill = new Skill();
        skill.setUserId(userId);
        skill.setName(dto.getName());
        skill.setLevel(dto.getLevel());
        skill.setCategory(dto.getCategory());
        skill.setSortOrder(dto.getSortOrder());
        skillMapper.insert(skill);
    }

    @Override
    public void update(Long id, SkillDto dto, Long userId) {
        Skill skill = skillMapper.findById(id);
        if (skill == null) {
            throw new RuntimeException("技能不存在");
        }
        if (!skill.getUserId().equals(userId)) {
            throw new RuntimeException("無權限修改此技能");
        }
        skill.setName(dto.getName());
        skill.setLevel(dto.getLevel());
        skill.setCategory(dto.getCategory());
        skill.setSortOrder(dto.getSortOrder());
        skillMapper.update(skill);
    }

    @Override
    public void delete(Long id, Long userId) {
        Skill skill = skillMapper.findById(id);
        if (skill == null) {
            throw new RuntimeException("技能不存在");
        }
        if (!skill.getUserId().equals(userId)) {
            throw new RuntimeException("無權限刪除此技能");
        }
        skillMapper.deleteById(id);
    }
}
