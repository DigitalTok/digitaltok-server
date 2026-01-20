package com.digital_tok.service;

import com.digital_tok.domain.Device;
import com.digital_tok.dto.request.DeviceRequestDTO;
import com.digital_tok.dto.response.DeviceResponseDTO;
import com.digital_tok.global.DeviceStatus;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * 기기 상태 조회
     */
    public DeviceResponseDTO.Result getDeviceStatus(Long deviceId) {
        // 기기를 데이터베이스에서 조회
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 기기의 상태와 등록 정보를 반환
        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }

    /**
     * 기기 연결 (활성화)
     */
    public DeviceResponseDTO.Result connectDevice(DeviceRequestDTO request) {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        if (device.getDeleted_at() != null) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        if (device.getStatus() == DeviceStatus.ACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED); // 이미 활성화된 상태
        }

        device.setStatus(DeviceStatus.ACTIVE);
        device.setRegisteredAt(LocalDateTime.now());
        device.setUpdated_at(LocalDateTime.now());

        device = deviceRepository.save(device);

        log.info("Device ID {} 활성화 완료.", device.getId());

        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .build();
    }

    /**
     * 기기 연결 해제 (비활성화)
     */
    public DeviceResponseDTO.Result disconnectDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        if (device.getDeleted_at() != null) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        if (device.getStatus() == DeviceStatus.INACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_DISCONNECTED); // 이미 비활성화된 상태
        }

        device.setStatus(DeviceStatus.INACTIVE);
        device.setUnregisteredAt(LocalDateTime.now());
        device.setUpdated_at(LocalDateTime.now());

        device = deviceRepository.save(device);

        log.info("Device ID {} 비활성화 완료.", device.getId());

        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }
}
