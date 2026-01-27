package com.digital_tok.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class ImageResponseDTO {

    /**
     * 최근 사용한 사진 조회 API
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentImageListDto {
        @Schema(description = "리스트에 포함된 이미지 개수", example = "3")
        private Integer count;

        @Schema(description = "최근 사용한 이미지 리스트")
        private List<RecentImageDto> items;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentImageDto {
        @Schema(description = "이미지 ID", example = "301")
        private Long imageId;

        @Schema(description = "이미지 미리보기 URL", example = "https://cdn.dirring.com/images/preview/301.png")
        private String previewUrl;

        //@Schema(description = "이미지 카테고리 (SUBWAY_PRESET / USER_PHOTO)", example = "SUBWAY_PRESET")
        //private String category;

        @Schema(description = "이미지 원본 파일명", example = "강남_2호선")
        private String imageName;

        @Schema(description = "즐겨찾기 여부", example = "true")
        private Boolean isFavorite;

        @Schema(description = "마지막 사용 시각", example = "2026-01-12T12:34:56")
        private LocalDateTime lastUsedAt;

        //@Schema(description = "지하철 템플릿 ID (없으면 null)", example = "12")
        //private Long subwayTemplateId;
    }

    /**
     * 이미지 업로드 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadResultDto {
        private UploadedImageDto image;
        private UploadedImageMappingDto imageMapping;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadedImageDto {
        @Schema(description = "이미지 ID", example = "55")
        private Long imageId;

        @Schema(description = "원본 이미지 URL", example = "/images/original/55.jpg")
        private String originalUrl;

        @Schema(description = "미리보기 URL", example = "null")
        private String previewUrl;

        @Schema(description = "E-ink 데이터 URL", example = "null")
        private String einkDataUrl;

        //@Schema(description = "카테고리", example = "USER_PHOTO")
        //private String category;

        @Schema(description = "이미지 이름", example = "myphoto_001")
        private String imageName;

        @Schema(description = "생성 일시")
        private LocalDateTime createdAt;

        @Schema(description = "삭제 일시")
        private LocalDateTime deletedAt;

        //@Schema(description = "지하철 템플릿 ID")
        //private Long subwayTemplateId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadedImageMappingDto {
        @Schema(description = "매핑 ID", example = "102")
        private Long userImageId;

        @Schema(description = "유저 ID", example = "7")
        private Long userId;

        @Schema(description = "이미지 ID", example = "55")
        private Long imageId;

        @Schema(description = "즐겨찾기 여부", example = "false")
        private Boolean isFavorite;

        @Schema(description = "저장 일시")
        private LocalDateTime savedAt;

        @Schema(description = "최근 사용 일시")
        private LocalDateTime lastUsedAt;
    }

    /**
     * 이미지 미리보기 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewResultDto {
        @Schema(description = "이미지 ID", example = "55")
        private Long imageId;

        @Schema(description = "미리보기 이미지 URL", example = "https://cdn.diring.com/images/preview/55.png")
        private String previewUrl;

        @Schema(description = "미리보기 갱신 일시", example = "2026-01-12T21:40:11")
        private LocalDateTime updatedAt;
    }

    /**
     * 기기로 보낼 바이너리 데이터 요청 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BinaryResultDto {
        @Schema(description = "이미지 ID", example = "55")
        private Long imageId;

        @Schema(description = "E-ink 바이너리 데이터 다운로드 URL", example = "https://cdn.diring.com/eink/55.bin")
        private String einkDataUrl;

        @Schema(description = "마지막 사용 일시", example = "2026-01-12T22:10:20")
        private LocalDateTime lastUsedAt;
    }

    // ImageResponseDTO.java 내부에 추가

    /**
     * 즐겨찾기 등록/해체 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteResultDto {
        @Schema(description = "유저 ID", example = "10")
        private Long userId;

        @Schema(description = "이미지 ID", example = "55")
        private Long imageId;

        @Schema(description = "즐겨찾기 여부", example = "true")
        private Boolean isFavorite;

        @Schema(description = "현재 즐겨찾기 수", example = "1")
        private Integer favoriteCount;

        @Schema(description = "갱신 일시", example = "2026-01-12T22:20:10")
        private LocalDateTime updatedAt;
    }

}
