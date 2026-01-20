package com.digital_tok.repository;

import com.digital_tok.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    // ID로 기기 정보를 조회
    Optional<Device> findById(Long id);
}
