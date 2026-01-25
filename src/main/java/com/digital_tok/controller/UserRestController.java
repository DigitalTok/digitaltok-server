package com.digital_tok.controller;

import com.digital_tok.dto.request.UserRequestDTO;
import com.digital_tok.dto.response.UserResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.global.security.PrincipalDetails; // Import 확인!
import com.digital_tok.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import 확인!
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
    public ApiResponse<String> withdraw(@AuthenticationPrincipal PrincipalDetails principal,
                                        @RequestBody UserRequestDTO.WithdrawDto request) {
        // principal.getUserId()로 실제 토큰의 주인 ID를 가져옴
        userService.withdraw(principal.getUserId(), request);
        return ApiResponse.onSuccess(SuccessCode.OK, "회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 2. 비밀번호 변경 API
     */
    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경 API", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    public ApiResponse<String> changePassword(@AuthenticationPrincipal PrincipalDetails principal,
                                              @RequestBody UserRequestDTO.ChangePasswordDto request) {
        userService.changePassword(principal.getUserId(), request);
        return ApiResponse.onSuccess(SuccessCode.OK, "비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 3. 이메일 주소 변경 API
     */
    @PatchMapping("/email")
    @Operation(summary = "이메일 변경 API", description = "기존 비밀번호 확인 후 새로운 이메일로 변경합니다.")
    public ApiResponse<String> changeEmail(@AuthenticationPrincipal PrincipalDetails principal,
                                           @RequestBody UserRequestDTO.ChangeEmailDto request) {
        userService.changeEmail(principal.getUserId(), request);
        return ApiResponse.onSuccess(SuccessCode.OK, "이메일 주소가 성공적으로 변경되었습니다.");
    }

    /**
     * 4. 내 프로필 조회 API
     */
    @GetMapping("/me")
    @Operation(summary = "내 프로필 조회 API", description = "로그인한 유저의 프로필 정보를 반환합니다.")
    public ApiResponse<UserResponseDTO.MyProfileDto> getMyProfile(@AuthenticationPrincipal PrincipalDetails principal) {
        // Service 호출로 변경됨
        UserResponseDTO.MyProfileDto profile = userService.getProfile(principal.getUserId());
        return ApiResponse.onSuccess(SuccessCode.OK, profile);
    }

    /**
     * 5. 닉네임 수정 API
     */
    @PatchMapping("/me")
    @Operation(summary = "닉네임 수정 API", description = "로그인한 유저의 닉네임을 수정합니다.")
    public ApiResponse<UserResponseDTO.NicknameResultDto> updateNickname(@AuthenticationPrincipal PrincipalDetails principal,
                                                                         @RequestBody UserRequestDTO.NicknameUpdateDto request) {
        userService.updateNickname(principal.getUserId(), request);

        // 수정된 정보를 다시 조회해서 반환하거나, null을 반환해도 됨 (여기선 null 유지)
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}