package com.digital_tok.user.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.security.PrincipalDetails;
import com.digital_tok.user.dto.UserRequestDTO;
import com.digital_tok.user.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "회원 관련 API (로그인, 회원가입, 정보 수정 등)")
public interface UserControllerDocs {

    @Operation(summary = "회원 탈퇴 API By 이승주 (개발 완료)", description = "비밀번호 검증 후 회원 정보를 삭제(또는 비활성화)합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원 탈퇴 성공"
            )
    })
    ApiResponse<String> withdraw(PrincipalDetails principal);

    @Operation(summary = "비밀번호 변경 API By 이승주 (개발 완료)", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경 성공"
            )
    })
    ApiResponse<String> changePassword(PrincipalDetails principal,
                                       @RequestBody UserRequestDTO.ChangePasswordDto request);

    @Operation(summary = "이메일 변경 API By 이승주 (개발 완료)", description = "기존 비밀번호 확인 후 새로운 이메일로 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이메일 변경 성공"
            )
    })
    ApiResponse<String> changeEmail(PrincipalDetails principal,
                                    @RequestBody UserRequestDTO.ChangeEmailDto request);

    @Operation(summary = "내 프로필 조회 API By 이승주, 조성하 (개발 완료)", description = "로그인한 유저의 프로필 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.MyProfileDto.class))
            )
    })
    ApiResponse<UserResponseDTO.MyProfileDto> getMyProfile(PrincipalDetails principal);
}