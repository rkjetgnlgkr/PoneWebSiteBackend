package com.pone.website.service;

import com.pone.website.dto.PortfolioDto;
import com.pone.website.entity.Portfolio;

import java.util.List;

public interface PortfolioService {
    List<Portfolio> findAll();
    void add(PortfolioDto dto);
    void update(Long id, PortfolioDto dto);
    void delete(Long id);
}
