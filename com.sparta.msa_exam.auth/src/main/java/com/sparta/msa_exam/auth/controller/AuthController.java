package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.SignInRequestDto;
import com.sparta.msa_exam.auth.dto.SignInResponseDto;
import com.sparta.msa_exam.auth.dto.SignUpRequestDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.ok(authService.signUp(requestDto));
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequestDto requestDto){
        String token = authService.signIn(requestDto.getUsername(), requestDto.getPassword());
        return ResponseEntity.ok(new SignInResponseDto(token));
    }
}
