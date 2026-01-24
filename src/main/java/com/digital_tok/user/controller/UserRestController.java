package com.digital_tok.user.controller;

import com.digital_tok.user.dto.UserRequestDTO;
import com.digital_tok.user.dto.UserResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "회원 관련 API (로그인, 회원가입, 정보 수정 등)")
public class UserRestController {

    // private final UserService userService; // 추후 서비스 의존성 주입 필요

    /**
     * 1. 회원 탈퇴 API
     * 설명: 비밀번호를 확인 후 회원을 탈퇴 처리(Soft Delete or Hard Delete)합니다.
     * 특징: URL에 ID를 포함하지 않고, Header의 토큰으로 유저를 식별합니다.
     */
    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴 API", description = "비밀번호 검증 후 회원 정보를 삭제(또는 비활성화)합니다.")
    public ApiResponse<String> withdraw(@RequestBody UserRequestDTO.WithdrawDto request) {
        // TODO: 회원 탈퇴 로직 구현
        return ApiResponse.onSuccess(SuccessCode.OK, "회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 2. 비밀번호 변경 API
     * 설명: 현재 비밀번호와 새로운 비밀번호를 받아 변경합니다.
     */
    @PatchMapping("/me/password")
    @Operation(summary = "비밀번호 변경 API", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    public ApiResponse<String> changePassword(@RequestBody UserRequestDTO.ChangePasswordDto request) {
        // TODO: 비밀번호 변경 로직 구현
        return ApiResponse.onSuccess(SuccessCode.OK, "비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 3. 이메일 주소 변경 API
     * 설명: 비밀번호 확인 후 새로운 이메일로 변경합니다.
     */
    @PatchMapping("/me/email")
    @Operation(summary = "이메일 변경 API", description = "기존 비밀번호 확인 후 새로운 이메일로 변경합니다.")
    public ApiResponse<String> changeEmail(@RequestBody UserRequestDTO.ChangeEmailDto request) {
        // TODO: 이메일 변경 로직 구현
        return ApiResponse.onSuccess(SuccessCode.OK, "이메일 주소가 성공적으로 변경되었습니다.");
    }

    /**
     * 4. 내 프로필 조회 API
     */
    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회 API", description = "로그인한 유저의 프로필 정보를 반환합니다.")
    public ApiResponse<UserResponseDTO.MyProfileDto> getMyProfile() {
        // TODO: 실제 서비스 로직 구현 필요 (더미 데이터 반환)
        UserResponseDTO.MyProfileDto profile = UserResponseDTO.MyProfileDto.builder()
                .userId(1L)
                .name("조성하")
                .nickname("조성하하하")
                .email("example@example.com")
                .phone("010-1234-5678")
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, profile);
    }
}