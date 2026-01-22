package com.digital_tok.service.image;

import com.digital_tok.domain.Image;
import com.digital_tok.domain.ImageMapping;
import com.digital_tok.repository.ImageMappingRepository;
import com.digital_tok.repository.ImageRepository;
import com.digital_tok.service.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMappingRepository imageMappingRepository;
    private final AmazonS3Manager s3Manager;

    /**
     * 이미지 업로드: S3 업로드 + DB(image, image_mapping) 저장
     * (로그인 전이므로 userId는 더미로 받는 상태)
     */
    public UploadResult uploadImage(MultipartFile file, String imageName, Long userId) {

        // 1) S3 업로드
        String originalUrl = s3Manager.uploadFile("images", file);
        String previewUrl = originalUrl; // 지금은 원본을 미리보기로 재사용(임시)

        LocalDateTime now = LocalDateTime.now();

        // 2) image 저장
        Image image = Image.builder()
                .originalUrl(originalUrl)
                .previewUrl(previewUrl)
                .einkDataUrl(null)
                //.category(category)
                .imageName(imageName)
                .createdAt(now)
                .deletedAt(null)
                //.subwayTemplate(null)
                .build();

        imageRepository.save(image);

        // 3) image_mapping 저장
        ImageMapping mapping = ImageMapping.builder()
                .userId(userId)
                .image(image)
                .isFavorite(false)
                .savedAt(now)
                .lastUsedAt(null)
                .build();

        imageMappingRepository.save(mapping);

        return new UploadResult(image, mapping);
    }

    public PreviewResult getPreview(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("image not found: " + imageId));

        if (image.getPreviewUrl() != null && !image.getPreviewUrl().isBlank()) {
            return new PreviewResult(image.getImageId(), image.getPreviewUrl(), LocalDateTime.now());
        }

        String generatedPreviewUrl = "https://cdn.diring.com/images/preview/" + imageId + ".png";
        image.updatePreviewUrl(generatedPreviewUrl);
        imageRepository.save(image);

        return new PreviewResult(image.getImageId(), generatedPreviewUrl, LocalDateTime.now());
    }

    public BinaryResult getBinary(Long userId, Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("image not found: " + imageId));

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

        String generatedEinkUrl = "https://cdn.diring.com/eink/" + imageId + ".bin";
        image.updateEinkDataUrl(generatedEinkUrl);
        imageRepository.save(image);

        return new BinaryResult(image.getImageId(), generatedEinkUrl, now);
    }

    // 반환용 record
    public record UploadResult(Image image, ImageMapping mapping) {}
    public record PreviewResult(Long imageId, String previewUrl, LocalDateTime updatedAt) {}
    public record BinaryResult(Long imageId, String einkDataUrl, LocalDateTime lastUsedAt) {}
}
