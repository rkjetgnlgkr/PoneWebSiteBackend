package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PortfolioDto {

    @NotBlank(message = "作品名稱不能為空")
    private String name;

    private String description;

    private String url;

    // 已上傳圖片的路徑列表
    private List<String> imagePaths;
}
