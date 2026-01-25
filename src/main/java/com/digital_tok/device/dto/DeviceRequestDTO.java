package com.digital_tok.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeviceRequestDTO {
    @NotBlank
    private String nfcUid; // NFC UID
}
