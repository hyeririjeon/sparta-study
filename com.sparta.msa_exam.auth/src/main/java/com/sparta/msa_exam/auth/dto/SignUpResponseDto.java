package com.sparta.msa_exam.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDto {

    private Long userId;
    private String userName;
    private String role;

}
