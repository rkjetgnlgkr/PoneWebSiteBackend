package com.pone.website.controller;

import com.pone.website.dto.UserSettingsDto;
import com.pone.website.entity.UserSettings;
import com.pone.website.service.UserSettingsService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/settings")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping
    public Result<UserSettings> get(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userSettingsService.getByUserId(userId));
    }

    @PutMapping
    public Result<Void> save(@RequestBody @Validated UserSettingsDto dto,
                             HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userSettingsService.save(userId, dto);
        return Result.success();
    }
}
