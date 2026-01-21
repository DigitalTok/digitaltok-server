package com.digital_tok.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class TemplateResponseDTO {

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
        private Long templateId;

        @Schema(description = "역 이름", example = "을지로3가")
        private String stationName;

        @Schema(description = "호선 이름", example = "2호선")
        private String lineName;

        @Schema(description = "미리보기 이미지 URL", example = "https://cdn.dirring.com/images/preview/301.png")
        private String templateImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SubwayDetailDto {
        @Schema(description = "지하철 템플릿 ID", example = "1")
        private Long templateId;

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

    // 교통약자 템플릿 관련 DTO

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class VulnerableListDto {
        @Schema(description = "조회된 템플릿 개수", example = "2")
        private Integer count;

        @Schema(description = "교통약자 템플릿 리스트")
        private List<VulnerableItemDto> items;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class VulnerableItemDto {
        @Schema(description = "템플릿 ID", example = "405")
        private Long templateId;

        @Schema(description = "교통약자 유형", example = "임산부")
        private String vulnerableType;

        @Schema(description = "미리보기 이미지 URL")
        private String templateImageUrl;
    }

    // TODO: VulnerableDetailDto 개발필요
}
