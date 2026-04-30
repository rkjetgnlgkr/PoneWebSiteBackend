package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Portfolio {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 關聯圖片（非資料庫欄位，由 MyBatis 組裝）
    private List<PortfolioImage> images;
}
