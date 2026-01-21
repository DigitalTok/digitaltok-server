package com.digital_tok.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

public class SubwayResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SubwayListDto {
        @Schema(description = "지하철 역 템플릿 개수", example = "3")
        private Integer count;

        @Schema(description = "지하철 역 템플릿 리스트")
        private List<SubwayItemDto> items;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SubwayItemDto {

        @Schema(description = "지하철 템플릿 ID", example = "301")
        private Long subwayTemplateId;

        @Schema(description = "역 이름", example = "을지로3가")
        private String stationName;

        @Schema(description = "호선 이름", example = "2호선")
        private String lineName;

        @Schema(description = "미리보기 이미지 URL", example = "https://cdn.dirring.com/images/preview/301.png")
        private String previewUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SubwayDetailDto {
        @Schema(description = "지하철 템플릿 ID", example = "1")
        private Long subwayTemplateId;

        @Schema(description = "역 이름", example = "강남")
        private String stationName;

        @Schema(description = "영어 역 이름", example = "강남")
        private String stationNameEng;

        @Schema(description = "호선 이름", example = "2호선")
        private String lineName;

        @Schema(description = "미리보기 이미지 URL")
        private String templateImageUrl;

        @Schema(description = "이미지 바이너리 데이터 URL")
        private String templateDataUrl;
    }
}
