package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.dto.SignUpRequestDto;
import com.sparta.msa_exam.auth.dto.SignUpResponseDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.entity.UserRoleEnum;
import com.sparta.msa_exam.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    private final HttpServletResponse httpServletResponse;
    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, HttpServletResponse httpServletResponse) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.httpServletResponse = httpServletResponse;
    }

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        User user = User.signupUser(requestDto.getUsername(), requestDto.getPassword(), requestDto.getRole());

        User savedUser = userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(savedUser.getId())
                .userName(savedUser.getUsername())
                .role(savedUser.getRole().getAuthority())
                .build();
    }

    public String signIn(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다.");
        }
        String token = createAccessToken(user.getUsername(), user.getRole());
        httpServletResponse.addHeader(AUTHORIZATION_HEADER, token);
        return token;
    }

    public String createAccessToken(String username, UserRoleEnum role) {
        return BEARER_PREFIX +
                Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

}
