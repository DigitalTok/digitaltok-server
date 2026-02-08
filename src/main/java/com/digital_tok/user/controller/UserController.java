package com.digital_tok.user.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.global.security.PrincipalDetails;
import com.digital_tok.user.dto.UserRequestDTO;
import com.digital_tok.user.dto.UserResponseDTO;
import com.digital_tok.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs{

    private final UserService userService;

    /**
     * 1. 회원 탈퇴 API
     */
    @Override
    @DeleteMapping("/me")
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND
    })
    public ApiResponse<String> withdraw(@AuthenticationPrincipal PrincipalDetails principal) {
        // principal.getUserId()로 실제 토큰의 주인 ID를 가져옴
        userService.withdraw(principal.getUserId());
        return ApiResponse.onSuccess(SuccessCode.OK, "회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 2. 비밀번호 변경 API
     */
    @Override
    @PatchMapping("/password")
    @ApiErrorCodes({
            ErrorCode.BAD_REQUEST,
            ErrorCode.MEMBER_NOT_FOUND
    })
    public ApiResponse<String> changePassword(@AuthenticationPrincipal PrincipalDetails principal,
                                              @RequestBody UserRequestDTO.ChangePasswordDto request) {
        userService.changePassword(principal.getUserId(), request);
        return ApiResponse.onSuccess(SuccessCode.OK, "비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 3. 이메일 주소 변경 API
     */
    @Override
    @PatchMapping("/email")
    @ApiErrorCodes({
            ErrorCode.BAD_REQUEST,             // 400
            ErrorCode.MEMBER_NOT_FOUND,        // 404
            ErrorCode.MEMBER_ALREADY_REGISTERED // 409 (이미 등록된 이메일)
    })
    public ApiResponse<String> changeEmail(@AuthenticationPrincipal PrincipalDetails principal,
                                           @RequestBody UserRequestDTO.ChangeEmailDto request) {
        userService.changeEmail(principal.getUserId(), request);
        return ApiResponse.onSuccess(SuccessCode.OK, request.getNewEmail());
    }

    /**
     * 4. 내 프로필 조회 API
     */
    @Override
    @GetMapping("/me")
    @ApiErrorCodes({
            ErrorCode.MEMBER_NOT_FOUND // 404
    })
    public ApiResponse<UserResponseDTO.MyProfileDto> getMyProfile(@AuthenticationPrincipal PrincipalDetails principal) {
        // Service 호출로 변경됨
        UserResponseDTO.MyProfileDto profile = userService.getProfile(principal.getUserId());
        return ApiResponse.onSuccess(SuccessCode.OK, profile);
    }
}