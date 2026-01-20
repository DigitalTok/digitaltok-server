package com.digital_tok.domain;

import com.digital_tok.global.DeviceStatus;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor // JPA 필수 기본 생성자
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키에 자동 증가 설정
    @Column(name = "device_id")
    private Long id;
    

    @Column(name = "nfc_uid", length = 50, unique = true)
    private String nfcUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DeviceStatus status; // ACTIVE / INACTIVE

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 일대일 매핑
    private TestUser user; // 연결된 사용자

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "unregistered_at")
    private LocalDateTime unregisteredAt; // 기기 연결 해제 일시

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; //기기 등록 일시

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 기기 연결 (사용자 추가)
     */
    public void connect(TestUser user) {
        if (this.status == DeviceStatus.ACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }
        this.user = user;
        this.status = DeviceStatus.ACTIVE;
        this.registeredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 기기 연결 해제 (사용자 제거)
     */
    public void disconnect() {
        if (this.status == DeviceStatus.INACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_DISCONNECTED);
        }
        this.user = null; // 사용자 연결 해제
        this.status = DeviceStatus.INACTIVE;
        this.unregisteredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

   
}
