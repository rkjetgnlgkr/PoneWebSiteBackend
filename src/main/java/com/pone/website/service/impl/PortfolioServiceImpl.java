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
    public List<Portfolio> findAll() {
        return portfolioMapper.findAll();
    }

    @Override
    @Transactional
    public void add(PortfolioDto dto) {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        portfolio.setUrl(dto.getUrl());
        portfolioMapper.insert(portfolio);

        // 儲存圖片
        saveImages(portfolio.getId(), dto.getImagePaths());
    }

    @Override
    @Transactional
    public void update(Long id, PortfolioDto dto) {
        Portfolio portfolio = portfolioMapper.findById(id);
        if (portfolio == null) {
            throw new RuntimeException("作品不存在");
        }
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        portfolio.setUrl(dto.getUrl());
        portfolioMapper.update(portfolio);

        // 重新設定圖片
        portfolioMapper.deleteImagesByPortfolioId(id);
        saveImages(id, dto.getImagePaths());
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
