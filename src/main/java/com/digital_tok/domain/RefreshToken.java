package com.digital_tok.domain;

import com.digital_tok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rt_key")
    private Long id;

    // User와 1:1 관계 (한 유저는 하나의 리프레시 토큰만 가짐)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_value", nullable = false)
    private String token;

    // 토큰 갱신 메서드
    public void updateToken(String token) {
        this.token = token;
    }
}