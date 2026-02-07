package com.digital_tok.device.domain;

import com.digital_tok.user.domain.TestUser;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 필수 기본 생성자
@Table(name = "device")
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
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true) // 외래 키 수정
    private User user;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "unregistered_at")
    private LocalDateTime unregisteredAt; // 기기 연결 해제 일시

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt; //기기 등록 일시

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    /**
     * 기기 연결
     */
    public void connect(User user) {
        validateConnectable();
        this.user = user;
        this.status = DeviceStatus.ACTIVE;
        this.registeredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 기기 연결 해제 (소유자만 가능)
     */
    public void disconnect(User user) {
        validateOwner(user);
        validateDisconnectable();
        this.user = null;
        this.status = DeviceStatus.INACTIVE;
        this.unregisteredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /* =======================
       검증 로직
       ======================= */

    private void validateConnectable() {
        if (this.status == DeviceStatus.ACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_CONNECTED);
        }
    }

    private void validateDisconnectable() {
        if (this.status == DeviceStatus.INACTIVE) {
            throw new GeneralException(ErrorCode.DEVICE_ALREADY_DISCONNECTED);
        }
    }

    private void validateOwner(User user) {
        if (this.user == null || !this.user.getId().equals(user.getId())) {
            throw new GeneralException(ErrorCode.DEVICE_NOT_FOUND);
        }
    }
   
}
