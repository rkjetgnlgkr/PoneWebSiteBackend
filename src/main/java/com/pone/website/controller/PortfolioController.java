package com.pone.website.controller;

import com.pone.website.dto.PortfolioDto;
import com.pone.website.entity.Portfolio;
import com.pone.website.service.PortfolioService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public Result<List<Portfolio>> list() {
        List<Portfolio> list = portfolioService.findAll();
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> add(@RequestBody @Validated PortfolioDto dto) {
        portfolioService.add(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody @Validated PortfolioDto dto) {
        portfolioService.update(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        portfolioService.delete(id);
        return Result.success();
    }
}
