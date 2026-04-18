package com.pone.website.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PortfolioImage {
    private Long id;
    private Long portfolioId;
    private String imagePath;
    private LocalDateTime createdAt;
}
