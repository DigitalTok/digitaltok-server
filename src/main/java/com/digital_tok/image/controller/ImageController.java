package com.digital_tok.image.controller;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.global.security.PrincipalDetails;
import com.digital_tok.image.converter.ImageConverter;
import com.digital_tok.image.dto.ImageRequestDTO;
import com.digital_tok.image.dto.ImageResponseDTO;
import com.digital_tok.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController implements ImageControllerDocs {

    private final ImageService imageService;
    private final ImageConverter imageConverter;

    private Long requireUserId(PrincipalDetails principal) {
        if (principal == null || principal.getUser() == null || principal.getUserId() == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        return principal.getUserId();
    }

    /**
     * 최근 사용한 사진 조회 API (토큰 기반)
     */
    @Override
    @GetMapping("/recent")
    @ApiErrorCodes({
            ErrorCode.UNAUTHORIZED,
            ErrorCode.IMAGE_BAD_REQUEST
    })
    public ApiResponse<ImageResponseDTO.RecentImageListDto> getRecentImages(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long userId = requireUserId(principal);

        var mappings = imageService.getRecentImageMappings(userId);
        var result = imageConverter.toRecentImageListDto(mappings);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 업로드 API (토큰 기반)
     */
    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiErrorCodes({
            ErrorCode.UNAUTHORIZED,
            ErrorCode.IMAGE_BAD_REQUEST,
            ErrorCode.IMAGE_UPLOAD_FAIL
            // NOTE: IMAGE_DERIVE_FAIL은 현재 정책상 throw 하지 않으므로 문서화에서 제외
    })
    public ApiResponse<ImageResponseDTO.UploadResultDto> uploadImage(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestPart("file") MultipartFile file,
            @RequestParam("imageName") String imageName
    ) {
        Long userId = requireUserId(principal);

        var r = imageService.uploadImage(file, imageName, userId);
        var result = imageConverter.toUploadResultDto(r);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 미리보기 조회 API
     * (유저 구분이 필요 없으면 토큰 없이도 가능)
     */
    @Override
    @GetMapping("/{imageId}/preview")
    @ApiErrorCodes({
            ErrorCode.IMAGE_BAD_REQUEST,
            ErrorCode.IMAGE_NOT_FOUND
    })
    public ApiResponse<ImageResponseDTO.PreviewResultDto> getImagePreview(
            @PathVariable Long imageId
    ) {
        var r = imageService.getPreview(imageId);
        var result = imageConverter.toPreviewResultDto(r);

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 바이너리 데이터 URL 조회 API (토큰 기반)
     */
    @Override
    @GetMapping("/{imageId}/binary")
    @ApiErrorCodes({
            ErrorCode.UNAUTHORIZED,
            ErrorCode.IMAGE_BAD_REQUEST,
            ErrorCode.IMAGE_NOT_FOUND,
            ErrorCode._INTERNAL_SERVER_ERROR
    })
    public ApiResponse<ImageResponseDTO.BinaryResultDto> getImageBinary(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long imageId
    ) {
        Long userId = requireUserId(principal);

        var r = imageService.getBinary(userId, imageId);
        var result = imageConverter.toBinaryResultDto(r);

        return ApiResponse.onSuccessResultOnly(result);
    }

    /**
     * 이미지 즐겨찾기 등록/해제 API (토큰 기반)
     */
    @Override
    @PatchMapping("/{imageId}/favorite")
    @ApiErrorCodes({
            ErrorCode.UNAUTHORIZED,
            ErrorCode.IMAGE_BAD_REQUEST,
            ErrorCode.IMAGE_MAPPING_NOT_FOUND,
            ErrorCode._INTERNAL_SERVER_ERROR
    })
    public ApiResponse<ImageResponseDTO.FavoriteResultDto> toggleFavorite(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long imageId,
            @RequestBody ImageRequestDTO.FavoriteDto request
    ) {
        Long userId = requireUserId(principal);

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
