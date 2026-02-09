package com.digital_tok.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeviceRequestDTO {
    @NotBlank(message = "NFC UID는 필수입니다.")
    private String nfcUid; // NFC UID
}
