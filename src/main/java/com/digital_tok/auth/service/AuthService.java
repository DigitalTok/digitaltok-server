package com.digital_tok.auth.service;

import com.digital_tok.auth.domain.RefreshToken;
import com.digital_tok.auth.dto.AuthRequestDTO;
import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.security.JwtTokenProvider;
import com.digital_tok.auth.repository.RefreshTokenRepository;
import com.digital_tok.user.domain.Role;
import com.digital_tok.user.domain.User;
import com.digital_tok.user.domain.UserStatus;
import com.digital_tok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 1. 회원가입
     */
    @Transactional
    public AuthResponseDTO.JoinResultDto join(AuthRequestDTO.JoinDto request) {
        // 1-1. 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }

        // 1-2. 닉네임 랜덤 생성 (예: User_a1b2c3d4)
        String randomNickname = "User_" + UUID.randomUUID().toString().substring(0, 8);

        // 혹시 모를 중복 방지를 위해 while문으로 체크 가능하나, UUID 8자리 충돌 확률은 매우 낮으므로 생략 가능.
        // 필요하다면 아래와 같이 추가
        while (userRepository.existsByNickname(randomNickname)) {
            randomNickname = "User_" + UUID.randomUUID().toString().substring(0, 8);
        }

        // 1-3. User 엔티티 생성 및 비밀번호 암호화
        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 암호화 필수
                .nickname(randomNickname) //랜덤 닉네임 적용
                //.name(request.getName())
                .phone(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .build();

        // 1-4. DB 저장
        User savedUser = userRepository.save(newUser);

        // 1-5. 응답 DTO 반환
        return AuthResponseDTO.JoinResultDto.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .build();
    }

    /**
     * 2. 로그인
     */
    @Transactional
    public AuthResponseDTO.LoginResultDto login(AuthRequestDTO.LoginDto request) {
        // 2-1. 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        // 2-2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 보안상 "비밀번호가 틀렸습니다"보다는 "정보가 일치하지 않습니다"가 좋으나, 편의상 명확한 에러 사용 가능
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        // 2-3. 토큰 생성 (Access + Refresh)
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 2-4. Refresh Token DB 저장 (기존 토큰이 있으면 업데이트, 없으면 생성)
        RefreshToken rt = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .build());

        rt.updateToken(refreshToken); // 더티 체킹으로 업데이트 or 새로 생성된 객체면 값 설정
        refreshTokenRepository.save(rt);

        // 2-5. 응답 DTO 반환
        return AuthResponseDTO.LoginResultDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(3600L) // 1시간 (설정과 맞춤)
                .build();
    }

    /**
     * 3. 로그아웃
     * 설명: DB에 저장된 Refresh Token을 삭제하여 더 이상 토큰 재발급이 불가능하게 함.
     */
    @Transactional
    public void logout(AuthRequestDTO.LogoutDto request) {
        // 3-1. 요청된 Refresh Token 유효성 검증 (옵션)
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        // 3-2. 토큰에서 User ID 추출
        Long userId = jwtTokenProvider.getUserId(request.getRefreshToken());

        // 3-3. DB에서 해당 유저의 Refresh Token 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }

    /**
     * 4. 이메일 중복 검사 (API용)
     * 사용 가능한 이메일이면 통과, 중복이면 예외 발생
     */
    public void checkEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GeneralException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }
}