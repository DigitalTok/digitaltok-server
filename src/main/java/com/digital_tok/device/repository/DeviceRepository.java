package com.digital_tok.device.repository;

import com.digital_tok.device.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    // NFC UID로 기기 정보를 조회
    Optional<Device> findByNfcUid(String nfcUid);
}
