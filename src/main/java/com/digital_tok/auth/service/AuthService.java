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
import lombok.extern.slf4j.Slf4j; // 로그 확인용
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender; // 메일 전송 객체 주입

    /**
     * 1. 회원가입
     */
    @Transactional
    public AuthResponseDTO.JoinResultDto join(AuthRequestDTO.JoinDto request) {
        // 1-1. 이메일로 유저 조회 (existsByEmail 대신 findByEmail 사용)
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 활동 중인 유저면 -> "이미 가입된 유저" 에러
            if (user.getStatus() == UserStatus.ACTIVE) {
                throw new GeneralException(ErrorCode.MEMBER_ALREADY_REGISTERED);
            }

            // 탈퇴한(INACTIVE) 유저면 -> "재가입(복구) 진행"
            // 닉네임 생성 등 필요한 로직 수행
            String randomNickname = "User_" + UUID.randomUUID().toString().substring(0, 8);

            // User 엔티티의 reactivate 메서드 호출 (비밀번호 암호화 필수!)
            user.reactivate(passwordEncoder.encode(request.getPassword()), randomNickname);

            // 기존 객체를 반환 (save 불필요, Dirty Checking으로 자동 업데이트)
            return AuthResponseDTO.JoinResultDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
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

        // 2-2. 탈퇴(INACTIVE) 또는 정지(BLOCKED)된 유저인지 확인
        if (user.getStatus() != UserStatus.ACTIVE) {
            // 적절한 에러 코드(예: MEMBER_NOT_FOUND 또는 커스텀 에러)를 던짐
            throw new GeneralException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 2-3. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 보안상 "비밀번호가 틀렸습니다"보다는 "정보가 일치하지 않습니다"가 좋으나, 편의상 명확한 에러 사용 가능
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        // 2-4. 토큰 생성 (Access + Refresh)
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 2-5. Refresh Token DB 저장 (기존 토큰이 있으면 업데이트, 없으면 생성)
        RefreshToken rt = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .build());

        rt.updateToken(refreshToken); // 더티 체킹으로 업데이트 or 새로 생성된 객체면 값 설정
        refreshTokenRepository.save(rt);

        // 2-6. 응답 DTO 반환
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

    /**
     * 비밀번호 재설정 (임시 비밀번호 발급 및 이메일 전송)
     */
    @Transactional
    public void resetPassword(AuthRequestDTO.ResetPasswordDto request) {
        // 1. 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 임시 비밀번호 생성 (랜덤 UUID 10자리)
        String tempPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        // 3. 비밀번호 암호화 및 DB 업데이트
        user.encodePassword(passwordEncoder.encode(tempPassword));

        // 4. 실제 이메일 전송
        sendTempPasswordEmail(user.getEmail(), tempPassword);
    }

    /**
     * [내부 메서드] 이메일 발송 로직
     */
    private void sendTempPasswordEmail(String to, String tempPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[DigitalTok] 임시 비밀번호 발급 안내");
            message.setText("안녕하세요. DigitalTok 서비스입니다.\n\n" +
                    "요청하신 임시 비밀번호는 아래와 같습니다.\n" +
                    "[" + tempPassword + "]\n\n" +
                    "로그인 후 반드시 비밀번호를 변경해 주세요.");

            javaMailSender.send(message); // 전송
            log.info("임시 비밀번호 메일 발송 성공: {}", to);

        } catch (Exception e) {
            log.error("메일 발송 실패: {}", e.getMessage());
            throw new GeneralException(ErrorCode._INTERNAL_SERVER_ERROR); // 메일 서버 에러 처리
        }
    }
}