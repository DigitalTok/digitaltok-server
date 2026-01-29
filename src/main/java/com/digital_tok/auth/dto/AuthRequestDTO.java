package com.digital_tok.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class AuthRequestDTO {
    // 1. 회원가입 요청
    @Getter
    public static class JoinDto {
        private String email;       // 필수
        private String password;    // 필수
        //private String nickname;    // 필수
        //private String name;        // 선택
        private String phoneNumber; // 선택
    }

    // 2. 로그인 요청
    @Getter
    public static class LoginDto {
        private String email;       // 필수
        private String password;    // 필수
    }

    // 3. 로그아웃 요청 (Body에 있는 Refresh Token 값)
    @Getter
    public static class LogoutDto {
        private String refreshToken; // 필수 (DB/Redis 삭제용)
    }

    // 4. 이메일 중복 확인 요청
    @Getter
    public static class CheckEmailDto {
        private String email;
    }

    @Getter
    public static class ResetPasswordDto {
        private String email;
    }
}
