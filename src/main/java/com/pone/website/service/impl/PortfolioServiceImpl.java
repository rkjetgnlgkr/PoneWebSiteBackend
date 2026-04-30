package com.pone.website.service.impl;

import com.pone.website.dto.PortfolioDto;
import com.pone.website.entity.Portfolio;
import com.pone.website.entity.PortfolioImage;
import com.pone.website.mapper.PortfolioMapper;
import com.pone.website.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Override
    public List<Portfolio> findAll(Long userId) {
        return portfolioMapper.findAll(userId);
    }

    @Override
    @Transactional
    public void add(PortfolioDto dto, Long userId) {
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        portfolio.setUrl(dto.getUrl());
        portfolioMapper.insert(portfolio);

        saveImages(portfolio.getId(), dto.getImagePaths());
    }

    @Override
    @Transactional
    public void update(Long id, PortfolioDto dto, Long userId) {
        Portfolio portfolio = portfolioMapper.findById(id);
        if (portfolio == null) {
            throw new RuntimeException("作品不存在");
        }
        if (!portfolio.getUserId().equals(userId)) {
            throw new RuntimeException("無權限修改此作品");
        }
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        portfolio.setUrl(dto.getUrl());
        portfolioMapper.update(portfolio);

        portfolioMapper.deleteImagesByPortfolioId(id);
        saveImages(id, dto.getImagePaths());
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Portfolio portfolio = portfolioMapper.findById(id);
        if (portfolio == null) {
            throw new RuntimeException("作品不存在");
        }
        if (!portfolio.getUserId().equals(userId)) {
            throw new RuntimeException("無權限刪除此作品");
        }
        portfolioMapper.deleteImagesByPortfolioId(id);
        portfolioMapper.deleteById(id);
    }

    private void saveImages(Long portfolioId, List<String> imagePaths) {
        if (!CollectionUtils.isEmpty(imagePaths)) {
            for (String path : imagePaths) {
                PortfolioImage image = new PortfolioImage();
                image.setPortfolioId(portfolioId);
                image.setImagePath(path);
                portfolioMapper.insertImage(image);
            }
        }
    }
}
