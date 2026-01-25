package com.digital_tok.repository;

import com.digital_tok.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 토큰 값으로 조회 (재발급 검증 시 사용)
    Optional<RefreshToken> findByToken(String token);

    // 유저 ID로 조회 (이미 존재하는 토큰이 있는지 확인용)
    Optional<RefreshToken> findByUserId(Long userId);

    // 유저 ID로 토큰 삭제 (로그아웃 시 사용)
    void deleteByUserId(Long userId);
}