package com.pone.website.mapper;

import com.pone.website.entity.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillMapper {

    List<Skill> findAll(@Param("userId") Long userId);

    Skill findById(Long id);

    void insert(Skill skill);

    void update(Skill skill);

    void deleteById(Long id);
}
