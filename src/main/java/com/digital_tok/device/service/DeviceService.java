package com.digital_tok.device.service;

import com.digital_tok.device.converter.DeviceConverter;
import com.digital_tok.device.domain.Device;
import com.digital_tok.device.dto.DeviceRequestDTO;
import com.digital_tok.device.dto.DeviceResponseDTO;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.user.domain.User;
import com.digital_tok.user.repository.UserRepository;
import com.digital_tok.device.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceService {

    private final DeviceConverter deviceConverter;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    /**
     * 기기 연결 메서드
     */
    public DeviceResponseDTO.Result connectDevice(DeviceRequestDTO request) {
        User user = getCurrentUser();
        Device device = findDevice(request.getNfcUid());

        device.connect(user);

        return deviceConverter.toResult(device);
    }

    /**
     * 기기 연결 해제 메서드
     */
    public DeviceResponseDTO.Result disconnectDevice(String nfcUid) {
        User user = getCurrentUser();
        Device device = findDevice(nfcUid);

        device.disconnect(user);

        return deviceConverter.toResult(device);
    }

    /**
     * 기기 상태 조회
     */
    public DeviceResponseDTO.Result getDeviceStatus(String nfcUid) {
        User user = getCurrentUser();
        Device device = findDevice(nfcUid);

        // 조회 권한 검증 (조회 정책)
        if (device.getUser() == null || !device.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        return deviceConverter.toResult(device);
    }

    /**
     * NFC 고유 Id로 기기 조회
     */

    private Device findDevice(String nfcUid) {
        return deviceRepository.findByNfcUid(nfcUid)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));
    }

    /**
     * 현재 인증된 사용자 조회
     */
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
