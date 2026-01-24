package com.digital_tok.image.domain;

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
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    public void touchLastUsedAt(LocalDateTime now) {
        this.lastUsedAt = now;
    }

    //notnull 일때 저장실패 막음
    @PrePersist
    public void prePersist() {
        if (this.savedAt == null) this.savedAt = LocalDateTime.now();
        if (this.isFavorite == null) this.isFavorite = false;
    }

}
