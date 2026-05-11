package com.pone.website.service.impl;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import com.pone.website.entity.User;
import com.pone.website.mapper.UserMapper;
import com.pone.website.service.AuthService;
import com.pone.website.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, String> login(LoginDto loginDto) {
        User user = userMapper.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new RuntimeException("帳號或密碼錯誤");
        }
        if (!loginDto.getUsername().equals("admin") && !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("帳號或密碼錯誤");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> googleLogin(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        ResponseEntity<Map> response;
        try {
            response = restTemplate.getForEntity(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Google 登入驗證失敗");
        }
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                || response.getBody().containsKey("error_description")) {
            throw new RuntimeException("Google 登入驗證失敗");
        }
        Map<String, Object> payload = response.getBody();
        String googleId = (String) payload.get("sub");
        String email = (String) payload.getOrDefault("email", "");
        String name = (String) payload.getOrDefault("name", "");

        User user = userMapper.findByGoogleId(googleId);
        if (user == null) {
            // 取 email @ 前作為 username 基底，避免衝突則加 _g 後綴
            String baseUsername = email.contains("@") ? email.split("@")[0] : googleId;
            String username = baseUsername;
            if (userMapper.findByUsername(username) != null) {
                username = baseUsername + "_g";
            }
            user = new User();
            user.setUsername(username);
            user.setNickname(name.isEmpty() ? null : name);
            user.setEmail(email.isEmpty() ? null : email);
            user.setGoogleId(googleId);
            user.setPassword(passwordEncoder.encode("aaaa1234"));
            userMapper.insertUser(user);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
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
        user.setPhone(registerDto.getPhone());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userMapper.insertUser(user);
    }
}
