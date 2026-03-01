package com.digital_tok.auth.converter;

import com.digital_tok.auth.dto.AuthResponseDTO;
import com.digital_tok.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class AuthConverter {

    public AuthResponseDTO.JoinResultDto toJoinResultDto(User user) {
        return AuthResponseDTO.JoinResultDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public AuthResponseDTO.LoginResultDto toLoginResultDto(String accessToken, String refreshToken) {
        return AuthResponseDTO.LoginResultDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(3600L)
                .build();
    }
}