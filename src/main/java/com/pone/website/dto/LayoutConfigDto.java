package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LayoutConfigDto {
    @NotBlank(message = "主題風格不可為空")
    @Pattern(regexp = "dark_star|nature|terminal", message = "主題風格不合法")
    private String themeStyle;
}
