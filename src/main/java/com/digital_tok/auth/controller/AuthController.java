package com.digital_tok.auth.controller;

import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 관련 API (회원가입, 로그인, 로그아웃)")
public class AuthController {

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
    public ApiResponse<AuthResponseDTO.LoginResultDto> login(@RequestBody @Valid AuthRequestDTO.LoginDto request) {
        AuthResponseDTO.LoginResultDto result = authService.login(request);
        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 3. 로그아웃 API
     */
    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "서버 DB 또는 Redis에 저장된 Refresh Token을 삭제합니다.")
    public ApiResponse<String> logout(@RequestBody @Valid AuthRequestDTO.LogoutDto request) {
        authService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.OK, "로그아웃에 성공했습니다.");
    }

    /**
     * 4. 이메일 중복 확인 API
     */
    @PostMapping("/duplicate-check")
    @Operation(summary = "이메일 중복 확인 API", description = "이메일을 받아 중복 여부를 확인합니다. (사용 가능: 200, 중복: 409)")
    public ApiResponse<String> checkEmail(@RequestBody @Valid AuthRequestDTO.CheckEmailDto request) {
        authService.checkEmailDuplicate(request.getEmail());
        return ApiResponse.onSuccess(SuccessCode.OK, "사용 가능한 이메일입니다.");
    }

    /**
     *  5. 비밀번호 재설정
     */
    @PostMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 랜덤한 숫자로 재설정합니다.")
    public ApiResponse<String> resetPassword(@RequestBody @Valid AuthRequestDTO.ResetPasswordDto request) {
        authService.resetPassword(request);
        return ApiResponse.onSuccess(SuccessCode.OK, "임시 비밀번호가 이메일로 전송되었습니다.");
    }
}