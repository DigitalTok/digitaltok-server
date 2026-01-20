package com.digital_tok.domain;

import com.digital_tok.global.DeviceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키에 자동 증가 설정
    private Long id;

    private String name; // 기기 이름

    private String nfcUid;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status; // 기기의 상태 (ACTIVE / INACTIVE)

    private LocalDateTime registeredAt; // 기기 연결 일시

    private LocalDateTime unregisteredAt; // 기기 연결 해제 일시

    private LocalDateTime created_at; //기기 등록 일시

    private LocalDateTime updated_at; //기기 수정 일시

    private LocalDateTime deleted_at; //기기 삭제 일시

    // 필요한 경우 추가 필드 정의
}
