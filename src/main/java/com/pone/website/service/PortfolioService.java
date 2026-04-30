package com.pone.website.service;

import com.pone.website.dto.PortfolioDto;
import com.pone.website.entity.Portfolio;

import java.util.List;

public interface PortfolioService {
    List<Portfolio> findAll(Long userId);
    void add(PortfolioDto dto, Long userId);
    void update(Long id, PortfolioDto dto, Long userId);
    void delete(Long id, Long userId);
}
