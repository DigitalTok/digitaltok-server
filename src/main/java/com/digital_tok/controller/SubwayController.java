package com.digital_tok.controller;

import com.digital_tok.convertor.TemplateConverter;
import com.digital_tok.domain.SubwayTemplate;
import com.digital_tok.dto.response.TemplateResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.service.SubwayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subway")
@RequiredArgsConstructor
@Tag(name = "Subway", description = "지하철 템플릿 관련 API")
public class SubwayController {

    private final SubwayService subwayService;
    private final TemplateConverter templateConverter;

    @GetMapping("/templates")
    @Operation(summary = "지하철 역 템플릿 목록 조회", description = "가나다 순으로 정렬된 지하철 역 템플릿 목록을 반환")
    public ApiResponse<TemplateResponseDTO.SubwayListDto> getSubwayTemplates() {

        List<SubwayTemplate> templates = subwayService.getSubwayTemplates();

        TemplateResponseDTO.SubwayListDto result = templateConverter.toSubwayListDto(templates);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @GetMapping("/templates/{subwayTemplateId}")
    @Operation(summary = "단일 지하철 역 조회", description = "특정 지하철 역 정보를 반환")
    public ApiResponse<TemplateResponseDTO.SubwayDetailDto> getSubwayTemplateDetail(
            @PathVariable Long subwayTemplateId) {

        SubwayTemplate subwayTemplate = subwayService.getSubwayTemplate(subwayTemplateId);

        TemplateResponseDTO.SubwayDetailDto result = templateConverter.toSubwayDetailDto(subwayTemplate);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }
}
