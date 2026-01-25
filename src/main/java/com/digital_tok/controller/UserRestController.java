package com.digital_tok.controller;

import com.digital_tok.dto.request.UserRequestDTO;
import com.digital_tok.dto.response.UserResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "회원 관련 API (로그인, 회원가입, 정보 수정 등)")
public class UserRestController {

    private final UserService userService;

    /**
     * 1. 회원 탈퇴 API
     */
    @DeleteMapping("/")
    @Operation(summary = "회원 탈퇴 API", description = "비밀번호 검증 후 회원 정보를 삭제(또는 비활성화)합니다.")
    public ApiResponse<String> withdraw(@RequestBody UserRequestDTO.WithdrawDto request) {
        // TODO: 추후 JWT Filter 구현 시 SecurityContextHolder에서 userId 추출 로직으로 변경 필요
        Long userId = 1L;

        userService.withdraw(userId, request);
        return ApiResponse.onSuccess(SuccessCode.OK, "회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 2. 비밀번호 변경 API
     */
    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경 API", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    public ApiResponse<String> changePassword(@RequestBody UserRequestDTO.ChangePasswordDto request) {
        Long userId = 1L; // 임시 ID

        userService.changePassword(userId, request);
        return ApiResponse.onSuccess(SuccessCode.OK, "비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 3. 이메일 주소 변경 API
     */
    @PatchMapping("/email")
    @Operation(summary = "이메일 변경 API", description = "기존 비밀번호 확인 후 새로운 이메일로 변경합니다.")
    public ApiResponse<String> changeEmail(@RequestBody UserRequestDTO.ChangeEmailDto request) {
        Long userId = 1L; // 임시 ID

        userService.changeEmail(userId, request);
        return ApiResponse.onSuccess(SuccessCode.OK, "이메일 주소가 성공적으로 변경되었습니다.");
    }

    /**
     * 4. 내 프로필 조회 API
     */
    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회 API", description = "로그인한 유저의 프로필 정보를 반환합니다.")
    public ApiResponse<UserResponseDTO.MyProfileDto> getMyProfile() {
        Long userId = 1L; // 임시 ID

        // UserService에 getProfile 메서드를 추가해야 합니다.
        // UserResponseDTO.MyProfileDto profile = userService.getProfile(userId);

        // 일단 컴파일 에러 방지를 위해 임시 더미 데이터 유지 (Service 구현 후 주석 해제하세요)
        UserResponseDTO.MyProfileDto profile = UserResponseDTO.MyProfileDto.builder()
                .userId(userId)
                .name("임시유저")
                .nickname("임시닉네임")
                .email("test@example.com")
                .phone("010-0000-0000")
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, profile);
    }

    /**
     * 5. 닉네임 수정 API
     */
    @PatchMapping("/me")
    @Operation(summary = "닉네임 수정 API", description = "로그인한 유저의 닉네임을 수정합니다.")
    public ApiResponse<UserResponseDTO.NicknameResultDto> updateNickname(@RequestBody UserRequestDTO.NicknameUpdateDto request) {
        Long userId = 1L; // 임시 ID

        // updateNickname 메서드는 반환값이 없으므로(void), 조회 로직을 별도로 호출하거나 수정해야 함.
        // 현재 로직상 수정만 수행
        userService.updateNickname(userId, request);

        return ApiResponse.onSuccess(SuccessCode.OK, null); // 닉네임 수정 완료 메시지 또는 조회 결과 반환
    }
}