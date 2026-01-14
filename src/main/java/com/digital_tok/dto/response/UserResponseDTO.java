package com.digital_tok.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class UserResponseDTO {

    // 1. 회원가입 성공 응답
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDto {
        private Long userId;        // 생성된 유저 PK
        private String email;       // 가입된 이메일
        private String nickname;    // 가입된 닉네임
        private String accessToken; // (선택) 가입 즉시 로그인 처리 시 필요
        private String refreshToken;// (선택)
    }

    // 2. 로그인 성공 응답
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDto {
        private String grantType;           // Bearer
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;  // 토큰 만료 시간
    }
}