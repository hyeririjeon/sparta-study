package com.sparta.msa_exam.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
