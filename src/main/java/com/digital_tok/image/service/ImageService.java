package com.digital_tok.image.service;

import com.digital_tok.global.AmazonS3Manager;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.image.domain.Image;
import com.digital_tok.image.domain.ImageMapping;
import com.digital_tok.image.handler.ImageProcessEvent;
import com.digital_tok.image.repository.ImageMappingRepository;
import com.digital_tok.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.digital_tok.image.domain.ImageStatus;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMappingRepository imageMappingRepository;
    private final ApplicationEventPublisher eventPublisher;
//    private final AmazonS3Manager s3Manager;
//    private final ImageDerivationService imageDerivationService;

    /**
     * 이미지 업로드: S3 업로드 + DB(image, image_mapping) 저장
     */
    @Transactional
    public UploadResult uploadImage(MultipartFile file, String imageName, Long userId) {

        // param validation
        validateParam(file, imageName, userId);

        LocalDateTime now = LocalDateTime.now();

        // 2. Image 객체 생성
        Image image = Image.builder()
                .imageName(imageName)
                .status(ImageStatus.PENDING) // 초기 상태: PENDING
                .createdAt(now)
                .deletedAt(null)
                .build();
        imageRepository.save(image);

        // 3. ImageMapping 객체 생성
        ImageMapping mapping = ImageMapping.builder()
                .userId(userId)
                .image(image)
                .isFavorite(false)
                .savedAt(now)
                .lastUsedAt(now)
                .build();
        imageMappingRepository.save(mapping);

        // 4. 비즈니스 로직(S3, Derivation)은 이벤트로 위임, 파일의 바이너리를 이벤트에 담아 보냄
        try {
            byte[] fileBytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String extension = (fileName != null && fileName.contains("."))
                    ? fileName.substring(fileName.lastIndexOf("."))
                    : "";
            eventPublisher.publishEvent(new ImageProcessEvent(image.getImageId(), fileBytes, file.getContentType(), extension));
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

        log.info("Image upload initialized. imageId={}, userId={}", image.getImageId(), userId);
//        byte[] originalBytes;
//        try {
//            originalBytes = file.getBytes();
//        } catch (Exception e) {
//            log.warn("Failed to read uploaded file bytes. imageName={}, userId={}", imageName, userId, e);
//            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
//        }
//
//        // 1) S3 업로드 (원본)
//        String originalUrl;
//        try {
//            originalUrl = s3Manager.uploadFile("images", file);
//        } catch (Exception e) {
//            log.error("S3 upload failed. imageName={}, userId={}", imageName, userId, e);
//            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
//        }
//
//        // 2) e-ink 파생 생성 (실패해도 업로드 성공 유지: fallback 정책)
//        ImageDerivationService.Result derived;
//        try {
//            derived = imageDerivationService.derive(new ByteArrayInputStream(originalBytes));
//            log.info("Derived eink assets done. previewUrl={}, einkDataUrl={}", derived.previewUrl(), derived.einkDataUrl());
//        } catch (Exception e) {
//            // 정책: 파생 실패해도 업로드 자체는 성공시키고 fallback
//            log.warn("Derive eink assets failed. fallback to originalUrl. imageName={}, userId={}", imageName, userId, e);
//            derived = new ImageDerivationService.Result(null, null);
//            // strict 정책으로 가려면 아래 주석 해제
//            // throw new GeneralException(ErrorCode.IMAGE_DERIVE_FAIL);
//        }
//
//
//        Image image = Image.builder()
//                .originalUrl(originalUrl)
//                .previewUrl(derived.previewUrl() != null ? derived.previewUrl() : originalUrl)
//                .einkDataUrl(derived.einkDataUrl())
//                .imageName(imageName)
//                .createdAt(now)
//                .deletedAt(null)
//                .build();
//
//        imageRepository.save(image);
//
//        ImageMapping mapping = ImageMapping.builder()
//                .userId(userId)
//                .image(image)
//                .isFavorite(false)
//                .savedAt(now)
//                .lastUsedAt(now)
//                .build();
//
//        imageMappingRepository.save(mapping);
//
//        log.info("Saved image & mapping. imageId={}, userImageId={}, userId={}",
//                image.getImageId(), mapping.getUserImageId(), userId);

        return new UploadResult(image, mapping);
    }

    // 이미지 validate 판단, invalide 시 Exception
    private void validateParam(MultipartFile file, String imageName, Long userId) {
        if (file == null || file.isEmpty() || imageName == null || imageName.isBlank() || userId == null) {
            throw new GeneralException(ErrorCode.IMAGE_BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public PreviewResult getPreview(Long imageId) {
        if (imageId == null) {
            throw new GeneralException(ErrorCode.IMAGE_BAD_REQUEST);
        }

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new GeneralException(ErrorCode.IMAGE_NOT_FOUND));

        // previewUrl 없으면 fallback으로 originalUrl
        String url = (image.getPreviewUrl() != null && !image.getPreviewUrl().isBlank())
                ? image.getPreviewUrl()
                : image.getOriginalUrl();

        log.debug("Preview requested. imageId={}, url={}", imageId, url);

        return new PreviewResult(image.getImageId(), url, LocalDateTime.now());
    }

    /**
     * binary 조회는 lastUsedAt 갱신/매핑 생성이 들어가서 readOnly 금지
     */
    @Transactional
    public BinaryResult getBinary(Long userId, Long imageId) {
        if (userId == null || imageId == null) {
            throw new GeneralException(ErrorCode.IMAGE_BAD_REQUEST);
        }

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new GeneralException(ErrorCode.IMAGE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        ImageMapping mapping = imageMappingRepository.findByUserIdAndImage_ImageId(userId, imageId)
                .orElseGet(() -> {
                    log.info("ImageMapping not found. create new mapping. userId={}, imageId={}", userId, imageId);
                    return ImageMapping.builder()
                            .userId(userId)
                            .image(image)
                            .isFavorite(false)
                            .savedAt(now)
                            .build();
                });

        try {
            mapping.touchLastUsedAt(now);
            imageMappingRepository.save(mapping);
        } catch (Exception e) {
            log.error("Failed to upsert image_mapping. userId={}, imageId={}", userId, imageId, e);
            throw new GeneralException(ErrorCode._INTERNAL_SERVER_ERROR);
        }

        String einkDataUrl = image.getEinkDataUrl();
        boolean hasEink = einkDataUrl != null && !einkDataUrl.isBlank();
        log.info("Binary requested. userId={}, imageId={}, hasEinkDataUrl={}", userId, imageId, hasEink);

        // 정책: einkDataUrl 없으면 null로 내려줌(현재 로직 유지)
        return new BinaryResult(
                image.getImageId(),
                hasEink ? einkDataUrl : null,
                now
        );
    }

    @Transactional(readOnly = true)
    public List<ImageMapping> getRecentImageMappings(Long userId) {
        if (userId == null) {
            throw new GeneralException(ErrorCode.IMAGE_BAD_REQUEST);
        }

        var list = imageMappingRepository
                .findByUserIdAndLastUsedAtIsNotNullAndImage_DeletedAtIsNullOrderByLastUsedAtDesc(
                        userId,
                        org.springframework.data.domain.PageRequest.of(0, 14)
                )
                .getContent();

        log.debug("Recent images requested. userId={}, count={}", userId, list.size());
        return list;
    }

    @Transactional
    public FavoriteUpdateResult updateFavorite(Long userId, Long imageId, Boolean isFavorite) {
        if (userId == null || imageId == null || isFavorite == null) {
            throw new GeneralException(ErrorCode.IMAGE_BAD_REQUEST);
        }

        ImageMapping mapping = imageMappingRepository.findByUserIdAndImage_ImageId(userId, imageId)
                .orElseThrow(() -> new GeneralException(ErrorCode.IMAGE_MAPPING_NOT_FOUND));

        try {
            mapping.updateFavorite(isFavorite);
            imageMappingRepository.save(mapping);
        } catch (Exception e) {
            log.error("Failed to update favorite. userId={}, imageId={}, isFavorite={}", userId, imageId, isFavorite, e);
            throw new GeneralException(ErrorCode._INTERNAL_SERVER_ERROR);
        }

        log.info("Favorite updated. userId={}, imageId={}, isFavorite={}", userId, imageId, isFavorite);
        return new FavoriteUpdateResult(mapping.getIsFavorite(), LocalDateTime.now());
    }

    // 반환용 record
    public record UploadResult(Image image, ImageMapping mapping) {}
    public record PreviewResult(Long imageId, String previewUrl, LocalDateTime updatedAt) {}
    public record BinaryResult(Long imageId, String einkDataUrl, LocalDateTime lastUsedAt) {}
    public record FavoriteUpdateResult(Boolean isFavorite, LocalDateTime updatedAt) {}
}
