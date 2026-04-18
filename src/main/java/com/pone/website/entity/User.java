package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
