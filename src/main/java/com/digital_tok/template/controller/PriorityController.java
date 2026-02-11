package com.digital_tok.template.controller;


import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
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
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class PriorityController implements PriorityControllerDocs {

    private final PriorityService priorityService;
    private final TemplateConverter templateConverter;

    @Override
    @GetMapping("/priority")
    public ApiResponse<PriorityResponseDTO.PriorityListDto> getPriorityTemplates() {

        List<PriorityTemplate> templates = priorityService.getPrioirtyTemplates();

        PriorityResponseDTO.PriorityListDto result = templateConverter.toPriorityListDto(templates);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @Override
    @GetMapping("/priority/{templateId}")
    @ApiErrorCodes({
            ErrorCode.TEMPLATE_NOT_FOUND // 404 (템플릿 없음)
    })
    public ApiResponse<PriorityResponseDTO.PriorityDetailDto> getPriorityTemplateDetail(
            @PathVariable Long templateId
    ) {

        PriorityTemplate prioirtyTemplate = priorityService.getPrioirtyTemplate(templateId);
        PriorityResponseDTO.PriorityDetailDto result = templateConverter.toPriorityDetailDto(prioirtyTemplate);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }
}
