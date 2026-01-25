package com.digital_tok.user.domain;

import com.digital_tok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate // 변경된 필드만 update 쿼리 날림
@DynamicInsert // null인 필드 제외하고 insert 쿼리 날림
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users") // MySQL 예약어 'user' 충돌 방지
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 50)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private UserStatus status; // ACTIVE, INACTIVE, BLOCKED

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    private Role role; // ROLE_USER, ROLE_ADMIN

    // 비즈니스 로직 (Setter 대신 메서드 사용)

    // 비밀번호 암호화 후 저장
    public void encodePassword(String password) {
        this.password = password;
    }

    // 정보 수정
    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    // 회원 탈퇴 (Soft Delete)
    public void withdraw() {
        this.status = UserStatus.INACTIVE;
    }

    // 이메일 변경
    public void updateEmail(String email) {
        this.email = email;}
}