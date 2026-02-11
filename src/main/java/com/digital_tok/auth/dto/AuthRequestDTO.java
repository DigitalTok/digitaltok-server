package com.digital_tok.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

public class AuthRequestDTO {
    // 1. 회원가입 요청
    @Getter
    public static class JoinDto {
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;       // 필수

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        private String password;    // 필수

        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호의 양식과 맞지 않습니다. 010-xxxx-xxxx")
        private String phoneNumber; // 선택
    }

    // 2. 로그인 요청
    @Getter
    public static class LoginDto {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;       // 필수

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        private String password;    // 필수
    }

    // 3. 로그아웃 요청 (Body에 있는 Refresh Token 값)
    @Getter
    public static class LogoutDto {
        @NotBlank
        private String refreshToken; // 필수 (DB/Redis 삭제용)
    }

    // 4. 이메일 중복 확인 요청
    @Getter
    public static class CheckEmailDto {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }

    // 5. 비밀번호 재설정
    @Getter
    public static class ResetPasswordDto {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }
}
