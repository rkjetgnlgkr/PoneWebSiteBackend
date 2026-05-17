package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SkillDto {

    @NotBlank(message = "技能名稱不能為空")
    private String name;

    @NotNull(message = "熟練度不能為空")
    @Min(value = 1, message = "熟練度最小為 1")
    @Max(value = 100, message = "熟練度最大為 100")
    private Integer level;

    private String category;

    private Integer sortOrder;
}
