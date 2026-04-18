package com.pone.website.controller;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import com.pone.website.service.AuthService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody @Validated LoginDto loginDto) {
        Map<String, String> data = authService.login(loginDto);
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated RegisterDto registerDto) {
        authService.register(registerDto);
        return Result.success();
    }
}
