package com.digital_tok.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subway_template")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SubwayTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_template_id")
    private Long subwayTemplateId;

    @Column(name = "station_name", length = 50, nullable = false)
    private String stationName;

    @Column(name = "line_name", length = 50, nullable = false)
    private String lineName;

    @Column(name = "template_image_url", length = 255, nullable = false)
    private String templateImageUrl;

    @Column(name = "template_preview_url", length = 255)
    private String templatePreviewUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "line_color", length = 50)
    private String lineColor;

    @Column(name = "station_name_eng", length = 50)
    private String stationNameEng;

    //db not null 컬럼 때문에 저장 실패하는 것 막아줌
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
