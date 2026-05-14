package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LayoutConfig {
    private Long id;
    private Long userId;
    private String themeStyle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
