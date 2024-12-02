package com.sparta.msa_exam.auth.dto;

import com.sparta.msa_exam.auth.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private UserRoleEnum role;
}
