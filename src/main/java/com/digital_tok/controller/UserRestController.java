package com.digital_tok.controller;

import com.digital_tok.dto.request.UserRequestDTO;
import com.digital_tok.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "회원 관련 API (로그인, 회원가입, 정보 수정 등)")
public class UserRestController {

    // private final UserService userService; // 추후 서비스 의존성 주입 필요

    /**
     * 1. 회원가입 API
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "이메일, 비밀번호, 닉네임 등을 받아 회원을 생성합니다.")
    public ApiResponse<UserResponseDTO.JoinResultDto> join(@RequestBody UserRequestDTO.JoinDto request) {
        // TODO: 회원가입 로직 구현
        return ApiResponse.onSuccess(null); // 응답 껍데기
    }

    /**
     * 2. 로그인 API
     * 설명: 이메일, 비밀번호를 받아 Access Token과 Refresh Token을 반환합니다.
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인하여 JWT(Access/Refresh Token)를 발급받습니다.")
    public ApiResponse<UserResponseDTO.LoginResultDto> login(@RequestBody UserRequestDTO.LoginDto request) {
        // TODO: 로그인 로직 구현
        return ApiResponse.onSuccess(null);
    }

    /**
     * 3. 로그아웃 API
     * 설명: DB의 Refresh Token을 삭제합니다. Header의 Access Token과 Body의 Refresh Token이 필요합니다.
     */
    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "서버 DB 또는 Redis에 저장된 Refresh Token을 삭제합니다.")
    public ApiResponse<String> logout(@RequestBody UserRequestDTO.LogoutDto request) {
        // TODO: 로그아웃 로직 구현 (DB에서 RefreshToken 삭제)
        return ApiResponse.onSuccess("로그아웃 성공");
    }

    /**
     * 4. 회원 탈퇴 API
     * 설명: 비밀번호를 확인 후 회원을 탈퇴 처리(Soft Delete or Hard Delete)합니다.
     * 특징: URL에 ID를 포함하지 않고, Header의 토큰으로 유저를 식별합니다.
     */
    @DeleteMapping("/")
    @Operation(summary = "회원 탈퇴 API", description = "비밀번호 검증 후 회원 정보를 삭제(또는 비활성화)합니다.")
    public ApiResponse<String> withdraw(@RequestBody UserRequestDTO.WithdrawDto request) {
        // TODO: 회원 탈퇴 로직 구현
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 5. 비밀번호 변경 API
     * 설명: 현재 비밀번호와 새로운 비밀번호를 받아 변경합니다.
     */
    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경 API", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    public ApiResponse<String> changePassword(@RequestBody UserRequestDTO.ChangePasswordDto request) {
        // TODO: 비밀번호 변경 로직 구현
        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 6. 이메일 주소 변경 API
     * 설명: 비밀번호 확인 후 새로운 이메일로 변경합니다.
     */
    @PatchMapping("/email")
    @Operation(summary = "이메일 변경 API", description = "기존 비밀번호 확인 후 새로운 이메일로 변경합니다.")
    public ApiResponse<String> changeEmail(@RequestBody UserRequestDTO.ChangeEmailDto request) {
        // TODO: 이메일 변경 로직 구현
        return ApiResponse.onSuccess("이메일이 성공적으로 변경되었습니다.");
    }
}