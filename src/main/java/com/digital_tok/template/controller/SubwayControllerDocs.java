package com.digital_tok.template.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.template.dto.SubwayCreateRequestDTO;
import com.digital_tok.template.dto.SubwayResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Subway", description = "지하철 템플릿 관련 API")
public interface SubwayControllerDocs {

    @Operation(summary = "전체 지하철 역 목록 조회 API By 이정헌 (개발 완료)", description = "가나다 순으로 정렬된 지하철 역 템플릿 목록을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SubwayResponseDTO.SubwayListDto.class))
            )
    })
    ApiResponse<SubwayResponseDTO.SubwayListDto> getSubwayTemplates();


    @Operation(summary = "단일 지하철 역 조회 API By 이정헌 (개발 완료)", description = "특정 지하철 역 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = SubwayResponseDTO.SubwayDetailDto.class))
            )
    })
    ApiResponse<SubwayResponseDTO.SubwayDetailDto> getSubwayTemplateDetail(@PathVariable Long templateId);


    @Operation(summary = "지하철 템플릿 검색 API By 이정헌 (개발 완료)", description = "역 이름(한글/영어)으로 템플릿을 검색합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "검색 성공 (결과가 없으면 빈 리스트 반환함)",
                    content = @Content(schema = @Schema(implementation = SubwayResponseDTO.SubwayListDto.class))
            )
    })
    ApiResponse<SubwayResponseDTO.SubwayListDto> searchSubwayTemplates(
            @Parameter(description = "검색할 키워드 ", example = "강남, Gang")
            @RequestParam(name = "keyword", required = false) String keyword
    );


    @Operation(summary = "지하철 역 이미지 생성 및 저장 By 이정헌 (개발 완료)", description = "지하철 역 정보를 입력하면 이미지를 생성하고 DB에 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이미지 생성 성공"
            )
    })
    ApiResponse<String> createSubwayTemplate(@RequestBody SubwayCreateRequestDTO request);
}