package com.digital_tok.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeviceResponseDTO {

    private final Boolean isSuccess; // 성공 여부
    @Getter
    @Builder
    public static class Result {
        private Long deviceId;              // 기기 ID
        private LocalDateTime registeredAt; // 등록 시점
        private LocalDateTime unregisteredAt; // 해제 시점
        private String status;              // ACTIVE / INACTIVE
    }
}
