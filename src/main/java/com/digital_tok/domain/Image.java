package com.digital_tok.domain;

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

    @Column(name = "original_url", length = 255, nullable = false)
    private String originalUrl;

    @Column(name = "preview_url", length = 255)
    private String previewUrl;

    @Column(name = "eink_data_url", length = 255)
    private String einkDataUrl;

    //@Column(name = "category", length = 50, nullable = false)
    //private String category;

    @Column(name = "image_name", length = 255, nullable = false)
    private String imageName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 지하철 템플릿 ID (FK)-삭제


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
