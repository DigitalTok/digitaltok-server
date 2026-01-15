package com.digital_tok.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

public class UserResponseDTO {

    // 추가: 회원가입, 수정, 탈퇴 등 CUD 작업 후 공통으로 쓸 결과 DTO
    // 프론트엔드 개발자분들이 필요하다고 하면 추가하면 될듯.
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResultDto {
        @Schema(description = "관련된 회원 ID", example = "1")
        Long userId;

        @Schema(description = "작업 완료 시각", example = "2026-01-15T14:30:00")
        LocalDateTime updatedAt;
    }

    // 내 프로필 조회 Response
    @Builder
    @Getter
    public static class MyProfileDto {
        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "유저 이름", example = "조성하")
        private String name;

        @Schema(description = "유저 닉네임", example = "조성하하하")
        private String nickname;

        @Schema(description = "유저 이메일", example = "example@example.com")
        private String email;

        @Schema(description = "유저 핸드폰 번호", example = "010-1234-5678")
        private String phone;
    }

    // 닉네임 수정 Response (변경된 프로필 정보)
    @Builder
    @Getter
    public static class NicknameResultDto {
        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "유저 이름", example = "조성하")
        private String name;

        @Schema(description = "변경된 닉네임", example = "새로운 닉네임")
        private String nickname;

        @Schema(description = "유저 이메일", example = "example@example.com")
        private String email;

        @Schema(description = "유저 핸드폰 번호", example = "010-1234-5678")
        private String phone;

        @Schema(description = "닉네임 변경 일시", example = "2026-01-15T14:30:00")
        private LocalDateTime updatedAt;
    }
}