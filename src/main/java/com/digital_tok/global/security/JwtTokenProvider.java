package com.digital_tok.global.security;

import com.digital_tok.user.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private SecretKey key;

    // 객체 초기화 후 비밀키를 Base64 디코딩하여 Key 객체로 변환
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Access Token 생성
    public String createAccessToken(Long userId, String email, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + (accessTokenValidityInSeconds * 1000));

        return Jwts.builder()
                .subject(email)                 // 토큰 제목 (Subject) -> 이메일
                .claim("userId", userId)     // 비공개 클레임 -> 유저 ID
                .claim("role", role.name())  // 비공개 클레임 -> 권한 정보
                .issuedAt(now)                  // 토큰 발행 시간
                .expiration(validity)           // 토큰 만료 시간
                .signWith(key)                  // 암호화 알고리즘 및 비밀키
                .compact();
    }

    // 2. Refresh Token 생성 (보안상 클레임을 최소화)
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + (refreshTokenValidityInSeconds * 1000));

        return Jwts.builder()
                .subject(String.valueOf(userId)) // 리프레시 토큰은 유저 ID만 담음
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // 3. 토큰에서 User ID 추출
    // 안전한 방법 (Number로 받아서 Long으로 변환) 수정
    public Long getUserId(String token) {
        // 1. 우선 Number 타입으로 가져옴 (Integer든 Long이든 다 받을 수 있음)
        Number userId = parseClaims(token).get("userId", Number.class);

        // 2. 값이 없으면 에러, 있으면 longValue()로 안전하게 변환
        if (userId == null) {
            throw new IllegalArgumentException("토큰에 userId가 없습니다.");
        }
        return userId.longValue();
    }

    // 4. 토큰에서 Email 추출
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // 5. 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 내부 메서드: 토큰 파싱 (Claims 추출)
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}