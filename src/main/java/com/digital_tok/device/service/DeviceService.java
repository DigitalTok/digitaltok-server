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
        log.debug("Initiating device connection for NFC UID: {}", request.getNfcUid());

        // 현재 인증된 사용자 조회
        User currentUser = getCurrentUser();

        // NFC UID로 기기 조회
        Device device = findValidDevice(request.getNfcUid());

        // 기기가 이미 연결된 경우 예외 처리
        if (device.getUser() != null) {
            log.warn("Device {} is already connected to user {}", device.getId(), device.getUser().getId());
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }

        // 기기를 사용자와 연결 및 저장
        device.connect(currentUser);
        deviceRepository.save(device);

        log.info("User {} successfully connected Device {}", currentUser.getId(), device.getId());

        return deviceConverter.toResult(device);
    }

    /**
     * 기기 연결 해제 메서드
     */
    public DeviceResponseDTO.Result disconnectDevice(String nfcUid) {
        log.debug("Initiating device disconnection for NFC UID: {}", nfcUid);

        // 현재 인증된 사용자 조회
        User currentUser = getCurrentUser();

        // NFC UID로 기기 조회
        Device device = findValidDevice(nfcUid);

        // 연결 권한 없는 경우 예외 처리
        if (device.getUser() == null || !device.getUser().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to disconnect device {} without permission", currentUser.getId(), device.getId());
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        // 사용자 연결 해제 및 저장
        device.disconnect();
        deviceRepository.save(device);

        log.info("User {} successfully disconnected Device {}", currentUser.getId(), device.getId());

        return deviceConverter.toResult(device);
    }

    /**
     * 기기 상태 조회
     */
    public DeviceResponseDTO.Result getDeviceStatus(String nfcUid) {
        log.debug("Fetching status for device with NFC UID: {}", nfcUid);

        // 현재 인증된 사용자 조회
        User currentUser = getCurrentUser();

        // NFC UID로 기기 조회 및 권한 확인
        Device device = findValidDevice(nfcUid);
        if (device.getUser() == null || !device.getUser().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to access status of device {} without permission", currentUser.getId(), device.getId());
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }

        // 기기 상태 반환
        return deviceConverter.toResult(device);
    }

    // ============================
    // Private Helper Methods
    // ============================

    /**
     * SecurityContext에서 현재 사용자를 가져오는 메서드
     */
    private User getCurrentUser() {
        String username = getAuthenticatedUsername();
        return userRepository.findByEmail(username)
            .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * SecurityContext에서 인증된 사용자명(email) 가져오기
     */
    private String getAuthenticatedUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * NFC UID로 기기 조회 및 유효성 검사 수행
     */
    private Device findValidDevice(String nfcUid) {
        return deviceRepository.findByNfcUid(nfcUid)
            .orElseThrow(() -> new GeneralException(ErrorCode.DEVICE_NOT_FOUND));
    }

}
