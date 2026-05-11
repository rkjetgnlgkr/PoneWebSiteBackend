package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class GoogleLoginDto {

    @NotBlank(message = "idToken 不能為空")
    private String idToken;
}
