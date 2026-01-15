package com.digital_tok.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

public class UserRequestDTO {

    // 1. 회원 탈퇴 요청 (본인 확인용 비밀번호)
    @Getter
    public static class WithdrawDto {
        private String password;    // 필수
    }

    // 2. 비밀번호 변경 요청
    @Getter
    public static class ChangePasswordDto {
        private String oldPassword; // 필수 (현재 비밀번호)
        private String newPassword; // 필수 (새로운 비밀번호)
    }

    // 3. 이메일 변경 요청
    @Getter
    public static class ChangeEmailDto {
        private String password;    // 필수 (본인 확인용)
        private String newEmail;    // 필수 (변경할 새 이메일)
    }

    // 4. 닉네임 변경 요청
    @Getter
    public static class NicknameUpdateDto {
        @Schema(description = "변경하려는 새로운 닉네임", example = "새로운 닉네임")
        private String nickname; // 유저 별명
    }
}