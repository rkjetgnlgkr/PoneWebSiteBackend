package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String googleId;
    private String lineId;
    private String password;
    private String title;
    private String bio;
    private String avatar;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
