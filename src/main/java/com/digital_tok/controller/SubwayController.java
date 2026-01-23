package com.digital_tok.controller;

import com.digital_tok.convertor.TemplateConverter;
import com.digital_tok.domain.SubwayTemplate;
import com.digital_tok.dto.request.SubwayCreateRequest;
import com.digital_tok.dto.response.TemplateResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.service.template.SubwayService;
import com.digital_tok.service.template.SubwayTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subway")
@RequiredArgsConstructor
@Tag(name = "Subway", description = "지하철 템플릿 관련 API")
public class SubwayController {

    private final SubwayService subwayService;
    private final SubwayTemplateService subwayTemplateService;
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

    /**
     *
     * application.yml: access-key, secret-key, region, bucket 정보가 올바르게 들어있는지 확인
     * Headless 모드: 서버 배포 시 JVM 옵션 -Djava.awt.headless=true 해야함
     */
    @PostMapping("/api/templates/subway")
    public ApiResponse<String> createSubwayTemplate(@RequestBody SubwayCreateRequest request) {

        Long templateId = subwayTemplateService.createAndSaveSubwayTemplate(
                request.getStationName(),
                request.getStationNameEng(),
                request.getLineName()
        );

        return ApiResponse.onSuccess(SuccessCode.OK, "성공적으로 생성되었습니다. ID: " + templateId);
    }
}
