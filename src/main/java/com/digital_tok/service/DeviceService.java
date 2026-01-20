package com.digital_tok.service;

import com.digital_tok.domain.Device;
import com.digital_tok.dto.request.DeviceRequestDTO;
import com.digital_tok.dto.response.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.digital_tok.domain.TestUser;
import com.digital_tok.repository.TestUserRepository; // TestUser 조회를 위한 Repository

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final TestUserRepository testUserRepository; // TestUser 관련 조회를 위한 Repository

    /**
     * 기기 연결
     */
    public DeviceResponseDTO.Result connectDevice(DeviceRequestDTO request) {
        // 1. 임시 사용자 조회
        Long dummyUserId = 1L; // 사용자 토큰 인증 대체용 임시 ID
        TestUser user = testUserRepository.findById(dummyUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        /* 2. 사용자가 이미 다른 기기와 연결되어 있는지 확인
        if (user.getDevice() != null) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }
         */

        // 3. 요청된 기기를 데이터베이스에서 조회
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 4. 기기가 이미 다른 사용자와 연결되어 있는 경우 예외 처리
        if (device.getUser() != null) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }

        // 5. 기기를 연결 (엔티티의 connect 메서드 활용)
        device.connect(user);
        deviceRepository.save(device);

        log.info("임시 User ID {}가 기기 ID {}와 연결되었습니다.", dummyUserId, device.getId());

        // 6. 연결 결과 반환
        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .build();
    }

    /**
     * 기기 연결 해제
     */
    public DeviceResponseDTO.Result disconnectDevice(Long deviceId) {
        // 1. 임시 사용자 조회
        Long dummyUserId = 1L; // 사용자 토큰 인증 대체용 임시 ID
        TestUser user = testUserRepository.findById(dummyUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 요청된 기기를 데이터베이스에서 조회
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 3. 연결되지 않았거나 현재 사용자와 다르면 예외 처리
        if (!device.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        // 4. 기기 연결 해제
        device.disconnect();
        deviceRepository.save(device);

        log.info("임시 User ID {}가 기기 ID {}와의 연결을 해제했습니다.", dummyUserId, deviceId);

        // 5. 연결 해제 결과 반환
        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }

    /**
     * 기기 상태 조회
     */
    public DeviceResponseDTO.Result getDeviceStatus(Long deviceId) {
        // 1. 임시 사용자 조회
        Long dummyUserId = 1L; // 사용자 토큰 인증 대체용 임시 ID
        TestUser user = testUserRepository.findById(dummyUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 요청된 기기를 데이터베이스에서 조회
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 3. 사용자와 연결된 기기인지 확인
        if (device.getUser() == null || !device.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        // 4. 기기 상태 반환
        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }
}
