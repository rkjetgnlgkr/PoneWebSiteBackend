package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class WorkExperienceDto {

    @NotBlank(message = "公司名稱不能為空")
    private String company;

    @NotBlank(message = "職位不能為空")
    private String position;

    @NotNull(message = "開始日期不能為空")
    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isCurrent;

    private String description;

    private Integer sortOrder;
}
