package com.digital_tok.auth.controller;

import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth") // /auth로 변경
@Tag(name = "Auth", description = "인증 관련 API (회원가입, 로그인, 로그아웃)")
public class AuthRestController {

    // private final AuthService authService; // 서비스 이름도 AuthService로 분리하는 것 추천

    /**
     * 1. 회원가입 API
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "이메일, 비밀번호, 닉네임 등을 받아 회원을 생성합니다.")
    public ApiResponse<AuthResponseDTO.JoinResultDto> join(@RequestBody AuthRequestDTO.JoinDto request) {
        // TODO: 회원가입 로직 구현

        // 일단 더미데이터 넣어둠 (프론트엔드 개발자가 어떤 result를 받을지 알 수 있음)
        AuthResponseDTO.JoinResultDto mockResult = AuthResponseDTO.JoinResultDto.builder()
                .userId(1L)
                .email("aoc05230@naver.com")
                .nickname("아이작")
                .accessToken("aaaabbbb")
                .refreshToken("aaaabbbb")
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, mockResult); // 응답 껍데기
    }

    /**
     * 2. 로그인 API
     * 설명: 이메일, 비밀번호를 받아 Access Token과 Refresh Token을 반환합니다.
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인하여 JWT(Access/Refresh Token)를 발급받습니다.")
    public ApiResponse<AuthResponseDTO.LoginResultDto> login(@RequestBody AuthRequestDTO.LoginDto request) {
        // TODO: 로그인 로직 구현

        // 예시 더미데이터2
        AuthResponseDTO.LoginResultDto mockResult = AuthResponseDTO.LoginResultDto.builder()
                .grantType("grantType")
                .accessToken("aaaabbbb")
                .refreshToken("aaaabbbb")
                .accessTokenExpiresIn(3600L)
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, mockResult);
    }

    /**
     * 3. 로그아웃 API
     * 설명: DB의 Refresh Token을 삭제합니다. Header의 Access Token과 Body의 Refresh Token이 필요합니다.
     */
    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "서버 DB 또는 Redis에 저장된 Refresh Token을 삭제합니다.")
    public ApiResponse<String> logout(@RequestBody AuthRequestDTO.LogoutDto request) {
        // TODO: 로그아웃 로직 구현 (DB에서 RefreshToken 삭제)


        return ApiResponse.onSuccess(SuccessCode.OK, "로그아웃에 성공했습니다.");
    }
}