package com.digital_tok.image.controller;

import com.digital_tok.image.service.ImageService;
import com.digital_tok.image.dto.ImageRequestDTO;
import com.digital_tok.image.dto.ImageResponseDTO;
import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.SuccessCode;
import com.digital_tok.global.AmazonS3Manager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import com.digital_tok.image.repository.ImageRepository;
import com.digital_tok.image.repository.ImageMappingRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image", description = "이미지 관련 API")
public class ImageController {

    private final ImageService imageService;
    private final AmazonS3Manager s3Manager;
    private final ImageRepository imageRepository;
    private final ImageMappingRepository imageMappingRepository;


    /**
     * S3 테스트용 이미지 업로드 API
     */
    /**@PostMapping(value = "/s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "S3 테스트용 이미지 업로드 API", description = "이미지 파일과 이름을 받아 서버에 업로드합니다.")
    public ApiResponse<ImageResponseDTO.UploadResultDto> uploadImageS3(
            @Parameter(description = "업로드할 이미지 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "이미지 이름", example = "myphoto_001")
            @RequestParam("imageName") String imageName
    ) {
        // 1) S3 업로드
        String uploadedUrl = s3Manager.uploadFile("images", file);

        // 2) image 테이블 저장
        Image savedImage = imageRepository.save(
                Image.builder()
                        .originalUrl(uploadedUrl)
                        .previewUrl(uploadedUrl) // 일단 same url로(임시)
                        .einkDataUrl(null)
                        //.category("USER_PHOTO")
                        .imageName(imageName)
                        .createdAt(LocalDateTime.now())
                        .deletedAt(null)
                        .build()
        );

        // 3) image_mapping 저장 (로그인 미구현이니까 userId 더미)
        Long userId = 1L;
        ImageMapping savedMapping = imageMappingRepository.save(
                ImageMapping.builder()
                        .userId(userId)
                        .image(savedImage)
                        .isFavorite(false)
                        .savedAt(LocalDateTime.now())
                        .lastUsedAt(null)
                        .build()
        );

        // 4) 응답 DTO 만들기 (진짜 ID를 넣어야 함!)
        ImageResponseDTO.UploadedImageDto imageDto = ImageResponseDTO.UploadedImageDto.builder()
                .imageId(savedImage.getImageId())
                .originalUrl(savedImage.getOriginalUrl())
                .previewUrl(savedImage.getPreviewUrl())
                .einkDataUrl(savedImage.getEinkDataUrl())
                //.category(savedImage.getCategory())
                .imageName(savedImage.getImageName())
                .createdAt(savedImage.getCreatedAt())
                .deletedAt(savedImage.getDeletedAt())
                //.subwayTemplateId(null)
                .build();

        ImageResponseDTO.UploadedImageMappingDto mappingDto = ImageResponseDTO.UploadedImageMappingDto.builder()
                .userImageId(savedMapping.getUserImageId())
                .userId(savedMapping.getUserId())
                .imageId(savedImage.getImageId())
                .isFavorite(savedMapping.getIsFavorite())
                .savedAt(savedMapping.getSavedAt())
                .lastUsedAt(savedMapping.getLastUsedAt())
                .build();

        ImageResponseDTO.UploadResultDto result = ImageResponseDTO.UploadResultDto.builder()
                .image(imageDto)
                .imageMapping(mappingDto)
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }**/

    @GetMapping("/recent")
    @Operation(summary = "최근 사용한 사진 조회 API", description = "사용자가 최근에 사용한 사진 목록을 조회합니다.")
    public ApiResponse<ImageResponseDTO.RecentImageListDto> getRecentImages(
            @RequestParam Long userId
    ) {
        ImageResponseDTO.RecentImageListDto result = imageService.getRecentImages(userId);
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

            //@Parameter(description = "카테고리", example = "USER_PHOTO")
            //@RequestParam("category") String category
    ) {
        var r = imageService.uploadImage(file, imageName, userId);

        ImageResponseDTO.UploadedImageDto imageDto = ImageResponseDTO.UploadedImageDto.builder()
                .imageId(r.image().getImageId())
                .originalUrl(r.image().getOriginalUrl())
                .previewUrl(r.image().getPreviewUrl())
                .einkDataUrl(r.image().getEinkDataUrl())
                //.category(r.image().getCategory())
                .imageName(r.image().getImageName())
                .createdAt(r.image().getCreatedAt())
                .deletedAt(r.image().getDeletedAt())
                //.subwayTemplateId(r.image().getSubwayTemplate() == null ? null : r.image().getSubwayTemplate().getSubwayTemplateId())
                .build();
        ImageResponseDTO.UploadedImageMappingDto mappingDto = ImageResponseDTO.UploadedImageMappingDto.builder()
                .userImageId(r.mapping().getUserImageId())
                .userId(r.mapping().getUserId())
                .imageId(r.image().getImageId())
                .isFavorite(r.mapping().getIsFavorite())
                .savedAt(r.mapping().getSavedAt())
                .lastUsedAt(r.mapping().getLastUsedAt())
                .build();

        ImageResponseDTO.UploadResultDto result = ImageResponseDTO.UploadResultDto.builder()
                .image(imageDto)
                .imageMapping(mappingDto)
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    /**
     * 이미지 미리보기 조회 API
     */
    @GetMapping("/{imageId}/preview")
    public ApiResponse<ImageResponseDTO.PreviewResultDto> getImagePreview(@PathVariable Long imageId) {

        var r = imageService.getPreview(imageId);

        ImageResponseDTO.PreviewResultDto result = ImageResponseDTO.PreviewResultDto.builder()
                .imageId(r.imageId())
                .previewUrl(r.previewUrl())
                .updatedAt(r.updatedAt())
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }

    // ImageRestController.java 내부에 추가

    /**
     * 이미지 바이너리 데이터 URL 조회 API
     * (바이너리 데이터를 S3에 올려두었다고 가정)
     *
     * - 선택한 이미지의 `eink_data_url`을 반환
     * - 서버 내부적으로 **`ImageMapping`**의 `last_used_at`을 현재 시간(NOW)으로 업데이트
     * - 만약 마켓 이미지를 처음 쓰는 거라면, `ImageMapping`에 데이터를 새로 생성(`INSERT`)하면서 시간을 기록
     * - if 이미 변환된 미리보기 링크 있으면 ) 바로 내려줌
     * - else if 없으면)로직 실행 후 내려줌
     */
    @GetMapping("/{imageId}/binary")
    @Operation(summary = "이미지 바이너리 데이터 조회 API", description = "기기로 전송할 변환된 E-ink 바이너리 파일(.bin)의 URL을 조회합니다.")
    public ApiResponse<ImageResponseDTO.BinaryResultDto> getImageBinary(
            @PathVariable Long imageId) {
        System.out.println("### HIT /binary imageId=" + imageId);
        Long userId = 1L;//더미 데이터-- 추후 jwt추출로 변경
        var r = imageService.getBinary(userId, imageId);
        System.out.println("### Controller r.einkDataUrl=" + r.einkDataUrl());

        ImageResponseDTO.BinaryResultDto result = ImageResponseDTO.BinaryResultDto.builder()
                .imageId(r.imageId())
                .einkDataUrl(r.einkDataUrl())
                .lastUsedAt(r.lastUsedAt())
                .build();
        System.out.println("### DTO result.einkDataUrl=" + result.getEinkDataUrl());


        System.out.println("### CONTROLLER r.class=" + r.getClass());
        System.out.println("### CONTROLLER r=" + r);
        System.out.println("### CONTROLLER r.hash=" + System.identityHashCode(r));
        System.out.println("### CONTROLLER r.eink=" + r.einkDataUrl());


        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }


    /**
     * 이미지 즐겨찾기 등록/해제 API
     */
    @PatchMapping("/{imageId}/favorite") // 수정이므로 PATCH 사용
    @Operation(summary = "이미지 즐겨찾기 등록/해제 API", description = "이미지의 즐겨찾기 상태를 변경합니다.")
    public ApiResponse<ImageResponseDTO.FavoriteResultDto> toggleFavorite(
            @PathVariable Long imageId,
            @RequestBody ImageRequestDTO.FavoriteDto request
    ) {

        // TODO: 실제 DB 업데이트 로직 구현 (isFavorite 값에 따라 등록/해제 처리)

        // 더미 데이터 생성
        ImageResponseDTO.FavoriteResultDto result = ImageResponseDTO.FavoriteResultDto.builder()
                .imageId(imageId)
                .isFavorite(request.getIsFavorite()) // 요청받은 값을 그대로 반환 (잘 반영됐는지 확인용)
                .favoriteCount(request.getIsFavorite() ? 2 : 1) // 즐겨찾기 등록하면 1 증가된 값 나오게함.
                .updatedAt(LocalDateTime.of(2026, 1, 12, 22, 20, 10))
                .build();

        return ApiResponse.onSuccess(SuccessCode.OK, result);
    }
}
