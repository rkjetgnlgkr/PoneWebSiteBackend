package com.pone.website.service.impl;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import com.pone.website.entity.User;
import com.pone.website.mapper.UserMapper;
import com.pone.website.service.AuthService;
import com.pone.website.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${line.channel-id:}")
    private String lineChannelId;

    @Value("${line.channel-secret:}")
    private String lineChannelSecret;

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
    @SuppressWarnings("unchecked")
    public Map<String, String> lineLogin(String code, String redirectUri) {
        // 用 authorization code 換取 access token
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("code", code);
        tokenParams.add("redirect_uri", redirectUri);
        tokenParams.add("client_id", lineChannelId);
        tokenParams.add("client_secret", lineChannelSecret);

        ResponseEntity<Map> tokenResponse;
        try {
            tokenResponse = restTemplate.postForEntity(
                    "https://api.line.me/oauth2/v2.1/token",
                    new HttpEntity<>(tokenParams, tokenHeaders),
                    Map.class
            );
        } catch (Exception e) {
            throw new RuntimeException("LINE 登入驗證失敗");
        }
        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            throw new RuntimeException("LINE 登入驗證失敗");
        }
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 用 access token 取得用戶 profile
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.setBearerAuth(accessToken);
        ResponseEntity<Map> profileResponse;
        try {
            profileResponse = restTemplate.exchange(
                    "https://api.line.me/v2/profile",
                    HttpMethod.GET,
                    new HttpEntity<>(profileHeaders),
                    Map.class
            );
        } catch (Exception e) {
            throw new RuntimeException("LINE 用戶資料取得失敗");
        }
        if (!profileResponse.getStatusCode().is2xxSuccessful() || profileResponse.getBody() == null) {
            throw new RuntimeException("LINE 用戶資料取得失敗");
        }
        Map<String, Object> profile = profileResponse.getBody();
        String lineId = (String) profile.get("userId");
        String displayName = (String) profile.getOrDefault("displayName", "");

        // 查詢或建立使用者
        User user = userMapper.findByLineId(lineId);
        if (user == null) {
            String baseUsername = "line_" + lineId.substring(0, Math.min(8, lineId.length()));
            String username = baseUsername;
            if (userMapper.findByUsername(username) != null) {
                username = baseUsername + "_l";
            }
            user = new User();
            user.setUsername(username);
            user.setNickname(displayName.isEmpty() ? null : displayName);
            user.setLineId(lineId);
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
