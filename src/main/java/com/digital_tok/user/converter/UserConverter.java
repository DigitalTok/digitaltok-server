package com.digital_tok.user.converter;

import com.digital_tok.user.domain.User;
import com.digital_tok.user.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserResponseDTO.MyProfileDto toMyProfileDto(User user) {
        return UserResponseDTO.MyProfileDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}