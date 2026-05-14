package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserSettingsDto {
    @NotBlank(message = "主題風格不可為空")
    private String themeStyle;
}
