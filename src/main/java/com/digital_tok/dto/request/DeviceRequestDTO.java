package com.digital_tok.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRequestDTO {
    @NotNull
    private Long deviceId; // 기기 ID
}
