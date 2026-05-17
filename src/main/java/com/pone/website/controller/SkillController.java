package com.pone.website.controller;

import com.pone.website.dto.SkillDto;
import com.pone.website.entity.Skill;
import com.pone.website.service.SkillService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public Result<List<Skill>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(skillService.findAll(userId));
    }

    @PostMapping
    public Result<Void> add(@RequestBody @Validated SkillDto dto,
                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        skillService.add(dto, userId);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody @Validated SkillDto dto,
                               HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        skillService.update(id, dto, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        skillService.delete(id, userId);
        return Result.success();
    }
}
