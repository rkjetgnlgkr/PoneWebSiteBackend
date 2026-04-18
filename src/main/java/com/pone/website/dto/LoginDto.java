package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank(message = "帳號不能為空")
    private String username;

    @NotBlank(message = "密碼不能為空")
    private String password;
}
