package com.digital_tok.user.service;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.user.domain.User;
import com.digital_tok.user.dto.UserRequestDTO;
import com.digital_tok.user.dto.UserResponseDTO;
import com.digital_tok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 1. 회원 탈퇴
     * 설명: 비밀번호 확인 후 상태를 INACTIVE로 변경 (Soft Delete)
     */
    @Transactional
    public void withdraw(Long userId, UserRequestDTO.WithdrawDto request) {
        User user = getUserById(userId);

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST); // 비밀번호 불일치
        }

        // 회원 탈퇴 처리 (Soft Delete)
        user.withdraw();
    }

    /**
     * 2. 비밀번호 변경
     * 설명: 기존 비밀번호 확인 -> 새 비밀번호 암호화 후 저장
     */
    @Transactional
    public void changePassword(Long userId, UserRequestDTO.ChangePasswordDto request) {
        User user = getUserById(userId);

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        // 새 비밀번호 암호화 및 변경
        user.encodePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    /**
     * 3. 이메일 주소 변경
     * 설명: 비밀번호 확인 -> 이메일 중복 검사 -> 변경
     */
    @Transactional
    public void changeEmail(Long userId, UserRequestDTO.ChangeEmailDto request) {
        User user = getUserById(userId);

        // 비밀번호 확인 (본인 인증)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        // 새로운 이메일 중복 검사
        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new GeneralException(ErrorCode.MEMBER_ALREADY_REGISTERED);
        }

        // 이메일 변경
        user.updateEmail(request.getNewEmail());
    }

    /**
     * 4. 닉네임 변경
     * 설명: 닉네임 중복 검사 -> 변경
     */
    @Transactional
    public void updateNickname(Long userId, UserRequestDTO.NicknameUpdateDto request) {
        User user = getUserById(userId);

        // 닉네임 중복 검사 (기존 닉네임과 다를 경우에만)
        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST); // 중복된 닉네임
        }

        // 닉네임 변경 (프로필 이미지는 유지)
        user.updateProfile(request.getNickname(), user.getProfileImageUrl());
    }

    // 내부 헬퍼 메서드: ID로 유저 찾기 (반복 코드 제거)
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 5. 내 프로필 조회
     */
    public UserResponseDTO.MyProfileDto getProfile(Long userId) {
        User user = getUserById(userId);

        return UserResponseDTO.MyProfileDto.builder()
                .userId(user.getId())
                //.name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}