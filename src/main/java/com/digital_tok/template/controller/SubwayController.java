package com.digital_tok.template.controller;

import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
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
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class SubwayController implements SubwayControllerDocs{

    private final SubwayService subwayService;
    private final TemplateConverter templateConverter;
    private final SubwayTemplateUploadService subwayTemplateUploadService;

    @Override
    @GetMapping("/subway")
    public ApiResponse<SubwayResponseDTO.SubwayListDto> getSubwayTemplates() {

        List<SubwayTemplate> templates = subwayService.getSubwayTemplates();

        SubwayResponseDTO.SubwayListDto result = templateConverter.toSubwayListDto(templates);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @Override
    @GetMapping("/subway/{templateId}")
    @ApiErrorCodes({
            ErrorCode.TEMPLATE_NOT_FOUND // 404 (템플릿 없음)
    })
    public ApiResponse<SubwayResponseDTO.SubwayDetailDto> getSubwayTemplateDetail(
            @PathVariable Long templateId) {

        SubwayTemplate subwayTemplate = subwayService.getSubwayTemplate(templateId);

        SubwayResponseDTO.SubwayDetailDto result = templateConverter.toSubwayDetailDto(subwayTemplate);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    @Override
    @GetMapping("/subway/search")
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
    @Override
    @PostMapping("/subway/generate")
    @ApiErrorCodes({
            ErrorCode.IMAGE_UPLOAD_FAIL,      // 500 (S3 업로드 실패)
            ErrorCode.IMAGE_TO_BINARY_ERROR   // 500 (바이너리 변환 실패)
    })
    public ApiResponse<String> createSubwayTemplate(@RequestBody SubwayCreateRequestDTO request) {

        Long templateId = subwayTemplateUploadService.createAndSaveSubwayTemplate(
                request.getStationName(),
                request.getStationNameEng(),
                request.getLineName()
        );

        return ApiResponse.onSuccess(SuccessCode.OK, "성공적으로 생성되었습니다. templateId: " + templateId);
    }
}
