package com.pone.website.controller;

import com.pone.website.dto.WorkExperienceDto;
import com.pone.website.entity.WorkExperience;
import com.pone.website.service.WorkExperienceService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/work-experiences")
public class WorkExperienceController {

    @Autowired
    private WorkExperienceService workExperienceService;

    @GetMapping
    public Result<List<WorkExperience>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(workExperienceService.findAll(userId));
    }

    @PostMapping
    public Result<Void> add(@RequestBody @Validated WorkExperienceDto dto,
                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        workExperienceService.add(dto, userId);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody @Validated WorkExperienceDto dto,
                               HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        workExperienceService.update(id, dto, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        workExperienceService.delete(id, userId);
        return Result.success();
    }
}
