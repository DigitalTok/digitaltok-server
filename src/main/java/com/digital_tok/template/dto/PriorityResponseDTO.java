package com.digital_tok.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class PriorityResponseDTO {

    // 교통약자 템플릿 관련 DTO

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PriorityListDto {
        @Schema(description = "조회된 템플릿 개수", example = "2")
        private Integer count;

        @Schema(description = "교통약자 템플릿 리스트")
        private List<PriorityItemDto> items;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PriorityItemDto {
        @Schema(description = "템플릿 ID", example = "405")
        private Long templateId;

        @Schema(description = "교통약자 유형", example = "임산부")
        private String priorityType;

        @Schema(description = "미리보기 이미지 URL")
        private String templateImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PriorityDetailDto {

        @Schema(description = "교통약자 템플릿 ID")
        private Long templateId;

        @Schema(description = "교통약자 유형")
        private String priority_type;

        @Schema(description = "미리보기 이미지 URL")
        private String templateImageUrl;

        @Schema(description = "이미지 바이너리 데이터 URL")
        private String templateDataUrl;
    }
}
