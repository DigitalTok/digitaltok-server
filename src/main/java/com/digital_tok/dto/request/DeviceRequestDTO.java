package com.digital_tok.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeviceRequestDTO {
    @NotBlank
    private String nfcUid; // NFC UID
}
