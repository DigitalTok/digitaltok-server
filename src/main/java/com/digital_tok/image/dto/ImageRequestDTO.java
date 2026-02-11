package com.digital_tok.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ImageRequestDTO {

    @Getter
    public static class FavoriteDto {
        @Schema(description = "즐겨찾기 설정 여부 (true: 등록, false: 해제)", example = "true")
        @NotNull(message = "즐겨찾기 설정 여부는 필수입니다(NotNull).")
        private Boolean isFavorite;
    }
}
