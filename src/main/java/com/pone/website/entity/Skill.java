package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Skill {
    private Long id;
    private Long userId;
    private String name;
    private Integer level;
    private String category;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
