package com.digital_tok.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

public class UserRequestDTO {

    // 1. 회원가입 요청
    @Getter
    public static class JoinDto {
        private String email;       // 필수
        private String password;    // 필수
        private String nickname;    // 필수
        private String name;        // 선택
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

    // 4. 회원 탈퇴 요청 (본인 확인용 비밀번호)
    @Getter
    public static class WithdrawDto {
        private String password;    // 필수
    }

    // 5. 비밀번호 변경 요청
    @Getter
    public static class ChangePasswordDto {
        private String oldPassword; // 필수 (현재 비밀번호)
        private String newPassword; // 필수 (새로운 비밀번호)
    }

    // 6. 이메일 변경 요청
    @Getter
    public static class ChangeEmailDto {
        private String password;    // 필수 (본인 확인용)
        private String newEmail;    // 필수 (변경할 새 이메일)
    }
}