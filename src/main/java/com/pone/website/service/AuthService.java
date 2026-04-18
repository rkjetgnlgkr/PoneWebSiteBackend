package com.pone.website.service;

import com.pone.website.dto.LoginDto;
import com.pone.website.dto.RegisterDto;
import java.util.Map;

public interface AuthService {
    Map<String, String> login(LoginDto loginDto);
    void register(RegisterDto registerDto);
}
