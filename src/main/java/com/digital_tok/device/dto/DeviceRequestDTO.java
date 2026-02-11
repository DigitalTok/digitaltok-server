package com.digital_tok.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeviceRequestDTO {
    @NotBlank(message = "NFC UID는 필수입니다.")
    // TODO: 우리 기기의 UID 형식이 맞는지 Pattern 어노테이션 통해서 검증하면 좋을듯
    private String nfcUid; // NFC UID
}
