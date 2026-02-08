package com.digital_tok.template.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.template.dto.PriorityResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Priority", description = "교통약자 템플릿 관련 API")
public interface PriorityControllerDocs {

    @Operation(summary = "전체 교통약자 템플릿 목록 조회 API", description = "전체 교통약자 템플릿 목록을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PriorityResponseDTO.PriorityListDto.class))
            )
    })
    ApiResponse<PriorityResponseDTO.PriorityListDto> getPriorityTemplates();


    @Operation(summary = "단일 교통약자 템플릿 조회 API", description = "단일 교통약자 템플릿의 상세정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PriorityResponseDTO.PriorityDetailDto.class))
            )
    })
    ApiResponse<PriorityResponseDTO.PriorityDetailDto> getPriorityTemplateDetail(@PathVariable Long templateId);
}