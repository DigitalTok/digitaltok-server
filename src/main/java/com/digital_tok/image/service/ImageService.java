package com.digital_tok.image.service;

import com.digital_tok.global.AmazonS3Manager;
import com.digital_tok.image.domain.Image;
import com.digital_tok.image.domain.ImageMapping;
import com.digital_tok.image.repository.ImageMappingRepository;
import com.digital_tok.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMappingRepository imageMappingRepository;
    private final AmazonS3Manager s3Manager;
    private final ImageDerivationService imageDerivationService;

    /**
     * 이미지 업로드: S3 업로드 + DB(image, image_mapping) 저장
     * (로그인 전이므로 userId는 더미로 받는 상태)
     *
     * TODO: 이미지 업로드하는 과정은 Transaction에서 제외하고,
     *       DB수정하는 부분만 Transaction처리하면 좋을거같음
     */
    public UploadResult uploadImage(MultipartFile file, String imageName, Long userId) {

        byte[] originalBytes;
        try {
            originalBytes = file.getBytes();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read uploaded file bytes", e);
        }

        // 1) S3 업로드 (원본)
        String originalUrl = s3Manager.uploadFile("images", file);

        // 2) e-ink 파생 생성
        ImageDerivationService.Result derived;
        try {
            System.out.println("### DERIVE START ###");
            derived = imageDerivationService.derive(new ByteArrayInputStream(originalBytes));
            System.out.println("### DERIVE DONE ### preview=" + derived.previewUrl() + " eink=" + derived.einkDataUrl());
        } catch (Exception e) {
            System.out.println("### DERIVE FAIL ###");
            e.printStackTrace();
            derived = new ImageDerivationService.Result(null, null);
        }

        LocalDateTime now = LocalDateTime.now();

        Image image = Image.builder()
                .originalUrl(originalUrl)
                .previewUrl(derived.previewUrl() != null ? derived.previewUrl() : originalUrl)
                .einkDataUrl(derived.einkDataUrl())
                .imageName(imageName)
                .createdAt(now)
                .deletedAt(null)
                .build();

        imageRepository.save(image);

        ImageMapping mapping = ImageMapping.builder()
                .userId(userId)
                .image(image)
                .isFavorite(false)
                .savedAt(now)
                .lastUsedAt(now)
                .build();

        imageMappingRepository.save(mapping);

        return new UploadResult(image, mapping);
    }

    @Transactional(readOnly = true)
    public PreviewResult getPreview(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("image not found: " + imageId));

        // previewUrl 없으면(예전 데이터) fallback으로 originalUrl이라도 반환
        String url = (image.getPreviewUrl() != null && !image.getPreviewUrl().isBlank())
                ? image.getPreviewUrl()
                : image.getOriginalUrl();

        return new PreviewResult(image.getImageId(), url, LocalDateTime.now());
    }

    @Transactional
    public BinaryResult getBinary(Long userId, Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("image not found: " + imageId));

        System.out.println("### getBinary imageId=" + imageId);
        System.out.println("### entity.einkDataUrl=" + image.getEinkDataUrl());
        System.out.println("### entity.previewUrl=" + image.getPreviewUrl());

        LocalDateTime now = LocalDateTime.now();

        ImageMapping mapping = imageMappingRepository.findByUserIdAndImage_ImageId(userId, imageId)
                .orElseGet(() -> ImageMapping.builder()
                        .userId(userId)
                        .image(image)
                        .isFavorite(false)
                        .savedAt(now)
                        .build());

        mapping.touchLastUsedAt(now);
        imageMappingRepository.save(mapping);

        if (image.getEinkDataUrl() != null && !image.getEinkDataUrl().isBlank()) {
            return new BinaryResult(image.getImageId(), image.getEinkDataUrl(), now);
        }

        return new BinaryResult(image.getImageId(), null, now);
    }

    /**
     * 최근 사용한 이미지 매핑 목록 조회 (Service는 Entity/List만 반환 - Template 스타일)
     */
    @Transactional(readOnly = true)
    public List<ImageMapping> getRecentImageMappings(Long userId) {

        return imageMappingRepository
                .findByUserIdAndLastUsedAtIsNotNullAndImage_DeletedAtIsNullOrderByLastUsedAtDesc(
                        userId,
                        org.springframework.data.domain.PageRequest.of(0, 14)
                )
                .getContent();
    }

    /**
     * 즐겨찾기 업데이트 (Service는 DTO 생성 X → 결과 record 반환)
     */
    @Transactional
    public FavoriteUpdateResult updateFavorite(Long userId, Long imageId, Boolean isFavorite) {

        if (isFavorite == null) {
            throw new IllegalArgumentException("isFavorite is required");
        }

        ImageMapping mapping = imageMappingRepository.findByUserIdAndImage_ImageId(userId, imageId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "image mapping not found. userId=" + userId + ", imageId=" + imageId
                ));

        mapping.updateFavorite(isFavorite);
        imageMappingRepository.save(mapping);

        return new FavoriteUpdateResult(mapping.getIsFavorite(), LocalDateTime.now());
    }

    // 반환용 record
    public record UploadResult(Image image, ImageMapping mapping) {}
    public record PreviewResult(Long imageId, String previewUrl, LocalDateTime updatedAt) {}
    public record BinaryResult(Long imageId, String einkDataUrl, LocalDateTime lastUsedAt) {}
    public record FavoriteUpdateResult(Boolean isFavorite, LocalDateTime updatedAt) {}
}
