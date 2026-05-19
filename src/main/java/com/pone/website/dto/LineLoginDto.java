package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LineLoginDto {

    @NotBlank(message = "code 不能為空")
    private String code;

    @NotBlank(message = "redirectUri 不能為空")
    private String redirectUri;
}
