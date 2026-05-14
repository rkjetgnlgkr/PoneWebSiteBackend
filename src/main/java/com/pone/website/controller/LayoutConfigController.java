package com.pone.website.controller;

import com.pone.website.dto.LayoutConfigDto;
import com.pone.website.entity.LayoutConfig;
import com.pone.website.service.LayoutConfigService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/settings")
public class LayoutConfigController {

    @Autowired
    private LayoutConfigService layoutConfigService;

    @GetMapping
    public Result<LayoutConfig> get(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(layoutConfigService.getByUserId(userId));
    }

    @PutMapping
    public Result<Void> save(@RequestBody @Validated LayoutConfigDto dto,
                             HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        layoutConfigService.save(userId, dto);
        return Result.success();
    }
}
