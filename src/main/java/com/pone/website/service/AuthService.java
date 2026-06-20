package com.pone.website.service;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import java.util.Map;

public interface AuthService {
    Map<String, String> login(LoginDto loginDto);
    void register(RegisterDto registerDto);
    Map<String, String> googleLogin(String idToken);

    Map<String, String> lineLogin(String code, String redirectUri);
}
