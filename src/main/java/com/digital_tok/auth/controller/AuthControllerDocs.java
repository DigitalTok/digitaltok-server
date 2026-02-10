package com.digital_tok.auth.controller;

import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 관련 API (회원가입, 로그인, 로그아웃)")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입 API By 이승주 (개발 완료)", description = "이메일, 비밀번호, 닉네임 등을 받아 회원을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.JoinResultDto.class))
            )
    })
    ApiResponse<AuthResponseDTO.JoinResultDto> join(@RequestBody AuthRequestDTO.JoinDto request);

    @Operation(summary = "로그인 API By 이승주 (개발 완료)", description = "이메일과 비밀번호로 로그인하여 JWT(Access/Refresh Token)를 발급받습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.LoginResultDto.class))
            )
    })
    ApiResponse<AuthResponseDTO.LoginResultDto> login(@RequestBody AuthRequestDTO.LoginDto request);

    @Operation(summary = "로그아웃 API By 이승주 (개발 완료)", description = "서버 DB 또는 Redis에 저장된 Refresh Token을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공"
            )
    })
    ApiResponse<String> logout(@RequestBody AuthRequestDTO.LogoutDto request);

    @Operation(summary = "이메일 중복 확인 API By 이승주 (개발 완료)", description = "이메일을 받아 중복 여부를 확인합니다. (사용 가능: 200, 중복: 409)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용 가능한 이메일"
            )
    })
    ApiResponse<String> checkEmail(@RequestBody AuthRequestDTO.CheckEmailDto request);

    @Operation(summary = "비밀번호 재설정 By 이승주 (개발 완료)", description = "비밀번호를 랜덤한 숫자로 재설정하고 이메일로 발송합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "임시 비밀번호 발송 성공"
            )
    })
    ApiResponse<String> resetPassword(@RequestBody AuthRequestDTO.ResetPasswordDto request);
}