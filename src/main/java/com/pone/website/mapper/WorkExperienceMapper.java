package com.pone.website.mapper;

import com.pone.website.entity.WorkExperience;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkExperienceMapper {

    List<WorkExperience> findAll(@Param("userId") Long userId);

    WorkExperience findById(Long id);

    void insert(WorkExperience workExperience);

    void update(WorkExperience workExperience);

    void deleteById(Long id);
}
