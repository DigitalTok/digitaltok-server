package com.digital_tok.device.service;

import com.digital_tok.device.domain.Device;
import com.digital_tok.device.dto.DeviceRequestDTO;
import com.digital_tok.device.dto.DeviceResponseDTO;
import com.digital_tok.user.domain.TestUser;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.device.repository.DeviceRepository;
import com.digital_tok.user.repository.TestUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final TestUserRepository testUserRepository;

    /**
     * 사용자 조회 (임시)
     * 토큰 기반 처리로 대체할 예정
     */
    private TestUser getCurrentUser() {
        // TODO: JWT 또는 SecurityContext에서 사용자 정보를 가져오는 로직으로 대체
        Long dummyUserId = 1L; // 임시 사용자 ID
        return testUserRepository.findById(dummyUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 기기 연결
     */
    public DeviceResponseDTO.Result connectDevice(DeviceRequestDTO request) {
        TestUser user = getCurrentUser(); // 현재 사용자

        // NFC UID로 기기 조회
        Device device = deviceRepository.findByNfcUid(request.getNfcUid())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 삭제된 기기는 사용할 수 없음
        if (device.getDeletedAt() != null) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        // 기기가 이미 특정 사용자와 연결된 경우
        if (device.getUser() != null) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }

        // 기기 연결
        device.connect(user);
        device = deviceRepository.save(device);

        log.info("사용자 {}가 기기 NFC UID {}를 연결했습니다.", user.getId(), request.getNfcUid());

        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .build();
    }

    /**
     * 기기 연결 해제
     */
    public DeviceResponseDTO.Result disconnectDevice(String nfcUid) {
        TestUser user = getCurrentUser(); // 현재 사용자

        // NFC UID로 기기 조회
        Device device = deviceRepository.findByNfcUid(nfcUid)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 이미 연결된 사용자가 없는 경우
        if (device.getUser() == null || !device.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND); // 연결 권한 없음
        }

        // 기기 연결 해제
        device.disconnect();
        device = deviceRepository.save(device);

        log.info("사용자 {}가 기기 NFC UID {}와의 연결을 해제했습니다.", user.getId(), nfcUid);

        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }

    /**
     * 기기 상태 조회
     */
    public DeviceResponseDTO.Result getDeviceStatus(String nfcUid) {
        TestUser user = getCurrentUser(); // 현재 사용자

        // NFC UID로 기기 조회
        Device device = deviceRepository.findByNfcUid(nfcUid)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));

        // 사용자와 연결된 기기인지 확인
        if (device.getUser() == null || !device.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND); // 권한 없음
        }

        return DeviceResponseDTO.Result.builder()
                .deviceId(device.getId())
                .status(device.getStatus().name())
                .registeredAt(device.getRegisteredAt())
                .unregisteredAt(device.getUnregisteredAt())
                .build();
    }
}
