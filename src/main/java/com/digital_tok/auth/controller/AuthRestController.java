package com.digital_tok.auth.controller;

import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API (회원가입, 로그인, 로그아웃)")
public class AuthRestController {

    private final AuthService authService;

    /**
     * 1. 회원가입 API
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "이메일, 비밀번호, 닉네임 등을 받아 회원을 생성합니다.")
    public ApiResponse<AuthResponseDTO.JoinResultDto> join(@RequestBody AuthRequestDTO.JoinDto request) {
        AuthResponseDTO.JoinResultDto result = authService.join(request);
        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 2. 로그인 API
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인하여 JWT(Access/Refresh Token)를 발급받습니다.")
    public ApiResponse<AuthResponseDTO.LoginResultDto> login(@RequestBody AuthRequestDTO.LoginDto request) {
        AuthResponseDTO.LoginResultDto result = authService.login(request);
        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 3. 로그아웃 API
     */
    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "서버 DB 또는 Redis에 저장된 Refresh Token을 삭제합니다.")
    public ApiResponse<String> logout(@RequestBody AuthRequestDTO.LogoutDto request) {
        authService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.OK, "로그아웃에 성공했습니다.");
    }
}