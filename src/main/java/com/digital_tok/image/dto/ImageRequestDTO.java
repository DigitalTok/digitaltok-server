package com.digital_tok.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class ImageRequestDTO {

    @Getter
    public static class FavoriteDto {
        @Schema(description = "즐겨찾기 설정 여부 (true: 등록, false: 해제)", example = "true")
        private Boolean isFavorite;
    }
}
