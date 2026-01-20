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

    @Column(name = "station_name_eng", length = 50)
    private String stationNameEng;

    @Column(name = "line_name", length = 50, nullable = false)
    private String lineName;

    @Column(name = "template_image_url", length = 255, nullable = false)
    private String templateImageUrl;

    @Column(name = "template_data_url", length = 255, nullable = false)
    private String templateDataUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
