package com.digital_tok.template.controller;


import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.template.convertor.TemplateConverter;
import com.digital_tok.template.domain.PriorityTemplate;
import com.digital_tok.template.dto.PriorityResponseDTO;
import com.digital_tok.template.service.PriorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Tag(name = "Priority", description = "교통약자 템플릿 관련 API")
public class PriorityController {

    private final PriorityService priorityService;
    private final TemplateConverter templateConverter;


    @GetMapping("/priority")
    @Operation(summary = "전체 교통약자 템플릿 목록 조회 API", description = "전체 교통약자 템플릿 목록을 반환합니다.")
    public ApiResponse<PriorityResponseDTO.PriorityListDto> getPriorityTemplates() {

        List<PriorityTemplate> templates = priorityService.getPrioirtyTemplates();

        PriorityResponseDTO.PriorityListDto result = templateConverter.toPriorityListDto(templates);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @GetMapping("/priority/{templateId}")
    @Operation(summary = "단일 교통약자 템플릿 목록 조회 API", description = "단일 교통약자 템플릿의 상세정보를 반환합니다.")
    public ApiResponse<PriorityResponseDTO.PriorityDetailDto> getPriorityTemplateDetail(
            @PathVariable Long templateId
    ) {

        PriorityTemplate prioirtyTemplate = priorityService.getPrioirtyTemplate(templateId);
        PriorityResponseDTO.PriorityDetailDto result = templateConverter.toPriorityDetailDto(prioirtyTemplate);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }
}
