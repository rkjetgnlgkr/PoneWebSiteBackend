package com.pone.website.mapper;

import com.pone.website.entity.Portfolio;
import com.pone.website.entity.PortfolioImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PortfolioMapper {

    List<Portfolio> findAll();

    Portfolio findById(Long id);

    void insert(Portfolio portfolio);

    void update(Portfolio portfolio);

    void deleteById(Long id);

    void insertImage(PortfolioImage image);

    void deleteImagesByPortfolioId(Long portfolioId);

    List<PortfolioImage> findImagesByPortfolioId(@Param("portfolioId") Long portfolioId);
}
