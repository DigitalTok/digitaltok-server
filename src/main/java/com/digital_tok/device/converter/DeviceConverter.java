package com.digital_tok.device.converter;

import com.digital_tok.device.domain.Device;
import com.digital_tok.device.dto.DeviceResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class DeviceConverter {
    public DeviceResponseDTO.Result toResult(Device device) {
        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }
}
