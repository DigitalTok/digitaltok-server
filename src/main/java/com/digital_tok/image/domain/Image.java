package com.digital_tok.image.domain;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "original_url", length = 2048, nullable = false)
    private String originalUrl;

    @Column(name = "preview_url", length = 2048)
    private String previewUrl;

    @Column(name = "eink_data_url", length = 2048)
    private String einkDataUrl;

    //@Column(name = "category", length = 50, nullable = false)
    //private String category;

    @Column(name = "image_name", length = 255, nullable = false)
    private String imageName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 이미지 정합성을 위해 추가
    @Column(name = "image_status")
    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    // 지하철 템플릿 ID (FK)-삭제

    // 업로드 완료 및 상태 업데이트 메서드
    public void completeUpload(String originalUrl, String previewUrl, String einkDataUrl, ImageStatus status) {
        this.originalUrl = originalUrl;
        this.previewUrl = previewUrl;
        this.einkDataUrl = einkDataUrl;
        this.status = status;
    }

    public void updatePreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void updateEinkDataUrl(String einkDataUrl) {
        this.einkDataUrl = einkDataUrl;
    }

    //notnull일때 저장 실패 막음
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

}
