package com.digital_tok.template.controller;

import com.digital_tok.template.convertor.TemplateConverter;
import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.template.dto.SubwayCreateRequestDTO;
import com.digital_tok.template.dto.SubwayResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.template.service.SubwayService;
import com.digital_tok.template.service.makeSubwayImage.SubwayTemplateUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Tag(name = "Subway", description = "지하철 템플릿 관련 API")
public class SubwayController {

    private final SubwayService subwayService;
    private final TemplateConverter templateConverter;
    private final SubwayTemplateUploadService subwayTemplateUploadService;

    @GetMapping("/subway")
    @Operation(summary = "전체 지하철 역 목록 조회 API", description = "가나다 순으로 정렬된 지하철 역 템플릿 목록을 반환")
    public ApiResponse<SubwayResponseDTO.SubwayListDto> getSubwayTemplates() {

        List<SubwayTemplate> templates = subwayService.getSubwayTemplates();

        SubwayResponseDTO.SubwayListDto result = templateConverter.toSubwayListDto(templates);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @GetMapping("/subway/{subwayTemplateId}")
    @Operation(summary = "단일 지하철 역 조회 API", description = "특정 지하철 역 정보를 반환")
    public ApiResponse<SubwayResponseDTO.SubwayDetailDto> getSubwayTemplateDetail(
            @PathVariable Long subwayTemplateId) {

        SubwayTemplate subwayTemplate = subwayService.getSubwayTemplate(subwayTemplateId);

        SubwayResponseDTO.SubwayDetailDto result = templateConverter.toSubwayDetailDto(subwayTemplate);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @GetMapping("/subway/search")
    @Operation(summary = "지하철 템플릿 검색 API", description = "역 이름(한글/영어)으로 템플릿을 검색")
    public ApiResponse<SubwayResponseDTO.SubwayListDto> searchSubwayTemplates(
            @Parameter(description = "검색할 키워드 (예: 강남, Gang)", example = "강남")
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        List<SubwayTemplate> searchResults = subwayService.searchSubwayTemplates(keyword);

        SubwayResponseDTO.SubwayListDto response = templateConverter.toSubwayListDto(searchResults);

        return ApiResponse.onSuccess(SuccessCode.OK, response);
    }

    /**
     *
     * application.yml: access-key, secret-key, region, bucket 정보가 올바르게 들어있는지 확인
     * Headless 모드: 서버 배포 시 JVM 옵션 -Djava.awt.headless=true 해야함
     */
    @PostMapping("/subway/generate")
    @Operation(summary = "지하철 역 이미지 생성 밎 저장", description = "지하철 역 정보를 입력하면 이미지를 생성하고 DB에 저장합니다.")
    public ApiResponse<String> createSubwayTemplate(@RequestBody SubwayCreateRequestDTO request) {

        Long templateId = subwayTemplateUploadService.createAndSaveSubwayTemplate(
                request.getStationName(),
                request.getStationNameEng(),
                request.getLineName()
        );

        return ApiResponse.onSuccess(SuccessCode.OK, "성공적으로 생성되었습니다. templateId: " + templateId);
    }
}
