package com.digital_tok.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequestDTO {

    // 1. 회원 탈퇴 요청 (본인 확인용 비밀번호) [삭제]
//    @Getter
//    public static class WithdrawDto {
//        private String password;    // 필수
//    }

    // 2. 비밀번호 변경 요청
    @Getter
    public static class ChangePasswordDto {
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        private String oldPassword; // 필수 (현재 비밀번호)

        @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        private String newPassword; // 필수 (새로운 비밀번호)
    }

    // 3. 이메일 변경 요청
    @Getter
    public static class ChangeEmailDto {
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        private String password;    // 필수 (본인 확인용)

        @NotBlank(message = "새로운 이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String newEmail;    // 필수 (변경할 새 이메일)
    }

    // 4. 닉네임 변경 요청 [삭제]
//    @Getter
//    public static class NicknameUpdateDto {
//        @Schema(description = "변경하려는 새로운 닉네임", example = "새로운 닉네임")
//        private String nickname; // 유저 별명
//    }
}