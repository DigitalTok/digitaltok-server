package com.digital_tok.auth.controller;

import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.auth.service.AuthService;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    /**
     * 1. 회원가입 API
     */
    @Override
    @PostMapping("/signup")
    @ApiErrorCodes({
            ErrorCode.MEMBER_ALREADY_REGISTERED // 이미 가입된 이메일인 경우
    })
    public ApiResponse<AuthResponseDTO.JoinResultDto> join(@RequestBody AuthRequestDTO.JoinDto request) {
        AuthResponseDTO.JoinResultDto result = authService.join(request);
        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 2. 로그인 API
     */
    @Override
    @PostMapping("/login")
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND, // 가입되지 않은 이메일
            ErrorCode.BAD_REQUEST       // 비밀번호 불일치
    })
    public ApiResponse<AuthResponseDTO.LoginResultDto> login(@RequestBody AuthRequestDTO.LoginDto request) {
        AuthResponseDTO.LoginResultDto result = authService.login(request);
        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 3. 로그아웃 API
     */
    @Override
    @DeleteMapping("/logout")
    @ApiErrorCodes({
            ErrorCode.BAD_REQUEST // 유효하지 않은 Refresh Token
    })
    public ApiResponse<String> logout(@RequestBody AuthRequestDTO.LogoutDto request) {
        authService.logout(request);
        return ApiResponse.onSuccess(SuccessCode.OK, "로그아웃에 성공했습니다.");
    }

    /**
     * 4. 이메일 중복 확인 API
     */
    @Override
    @PostMapping("/duplicate-check")
    @ApiErrorCodes({
            ErrorCode.MEMBER_ALREADY_REGISTERED // 이미 존재하는 이메일 (409)
    })
    public ApiResponse<String> checkEmail(@RequestBody AuthRequestDTO.CheckEmailDto request) {
        authService.checkEmailDuplicate(request.getEmail());
        return ApiResponse.onSuccess(SuccessCode.OK, "사용 가능한 이메일입니다.");
    }

    /**
     * 5. 비밀번호 재설정 API
     */
    @Override
    @PostMapping("/password/reset")
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND,      // 가입되지 않은 이메일
            ErrorCode._INTERNAL_SERVER_ERROR // 메일 전송 실패
    })
    public ApiResponse<String> resetPassword(@RequestBody AuthRequestDTO.ResetPasswordDto request) {
        authService.resetPassword(request);
        return ApiResponse.onSuccess(SuccessCode.OK, "임시 비밀번호가 이메일로 전송되었습니다.");
    }
}