package com.pone.website.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotBlank(message = "帳號不能為空")
    @Size(min = 3, max = 50, message = "帳號長度需在 3~50 字元之間")
    private String username;

    private String nickname;

    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, message = "密碼至少需要 6 個字元")
    private String password;
}
