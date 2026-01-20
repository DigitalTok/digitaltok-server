package com.digital_tok.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "image_mapping")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ImageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Long userImageId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    // nullable 하게 수정
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_template_id") // nullable
    private SubwayTemplate subwayTemplate;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    public void touchLastUsedAt(LocalDateTime now) {
        this.lastUsedAt = now;
    }
}
