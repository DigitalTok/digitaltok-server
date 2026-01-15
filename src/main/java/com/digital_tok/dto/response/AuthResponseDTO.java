package com.digital_tok.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

public class AuthResponseDTO {
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

        /**
         * grantType 예시 및 특징
         * authorization_code: 가장 많이 사용되며, 사용자 로그인 후 받은 인가 코드(code)를 이용해 토큰을 교환하는 방식 (보안 우수).
         * client_credentials: 사용자가 아닌 클라이언트(서비스) 자체의 자격 증명으로 토큰을 받는 방식 (서비스 간 통신).
         * password: 사용자 ID와 비밀번호를 직접 입력하여 토큰을 받는 방식 (자체 서비스 내에서만 권장).
         * refresh_token: 만료된 Access Token 대신 Refresh Token을 사용하여 새로운 Access Token을 발급받는 방식.
         * implicit: 브라우저 기반 클라이언트(JavaScript)에서 코드 없이 바로 토큰을 받는 방식 (보안 문제로 권장되지 않음).
         * */
        private String grantType;           // Bearer
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;  // 토큰 만료 시간
    }
}
