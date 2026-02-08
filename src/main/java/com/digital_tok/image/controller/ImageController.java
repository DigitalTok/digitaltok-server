package com.digital_tok.image.controller;

import com.digital_tok.image.converter.ImageConverter;
import com.digital_tok.image.dto.ImageRequestDTO;
import com.digital_tok.image.dto.ImageResponseDTO;
import com.digital_tok.image.service.ImageService;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image", description = "이미지 관련 API")
public class ImageController {

    private final ImageService imageService;
    private final ImageConverter imageConverter;

    /**
     * 최근 사용한 사진 조회 API
     * - Service: List<ImageMapping> 반환
     * - Controller: Converter로 DTO 변환
     */
    @GetMapping("/recent")
    @Operation(summary = "최근 사용한 사진 조회 API", description = "사용자가 최근에 사용한 사진 목록을 조회합니다.")
    public ApiResponse<ImageResponseDTO.RecentImageListDto> getRecentImages(@RequestParam Long userId) {

        var mappings = imageService.getRecentImageMappings(userId);
        var result = imageConverter.toRecentImageListDto(mappings);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 업로드 API
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드 API", description = "이미지 파일과 이름을 받아 서버에 업로드합니다.")
    public ApiResponse<ImageResponseDTO.UploadResultDto> uploadImage(
            @Parameter(description = "업로드할 이미지 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "이미지 이름", example = "myphoto_001")
            @RequestParam("imageName") String imageName,
            @RequestParam("userId") Long userId
    ) {
        var r = imageService.uploadImage(file, imageName, userId);
        var result = imageConverter.toUploadResultDto(r);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 미리보기 조회 API
     */
    @GetMapping("/{imageId}/preview")
    public ApiResponse<ImageResponseDTO.PreviewResultDto> getImagePreview(@PathVariable Long imageId) {

        var r = imageService.getPreview(imageId);
        var result = imageConverter.toPreviewResultDto(r);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 바이너리 데이터 URL 조회 API
     */
    @GetMapping("/{imageId}/binary")
    @Operation(summary = "이미지 바이너리 데이터 조회 API", description = "기기로 전송할 변환된 E-ink 바이너리 파일(.bin)의 URL을 조회합니다.")
    public ApiResponse<ImageResponseDTO.BinaryResultDto> getImageBinary(@PathVariable Long imageId) {

        Long userId = 1L; // 더미 데이터-- 추후 jwt 추출로 변경
        var r = imageService.getBinary(userId, imageId);

        var result = imageConverter.toBinaryResultDto(r);

        // 기존 컨벤션 유지: result만 내려주는 형태
        return ApiResponse.onSuccessResultOnly(result);
    }

    /**
     * 이미지 즐겨찾기 등록/해제 API
     * - Service: FavoriteUpdateResult record 반환
     * - Controller: Converter로 FavoriteResultDto 변환
     */
    @PatchMapping("/{imageId}/favorite")
    @Operation(summary = "이미지 즐겨찾기 등록/해제 API", description = "이미지의 즐겨찾기 상태를 변경합니다.")
    public ApiResponse<ImageResponseDTO.FavoriteResultDto> toggleFavorite(
            @PathVariable Long imageId,
            @RequestParam Long userId,
            @RequestBody ImageRequestDTO.FavoriteDto request
    ) {
        var r = imageService.updateFavorite(userId, imageId, request.getIsFavorite());

        var result = imageConverter.toFavoriteResultDto(
                userId,
                imageId,
                r.isFavorite(),
                r.updatedAt()
        );

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }
}
