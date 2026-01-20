package com.digital_tok.service.image;

import com.digital_tok.domain.Image;
import com.digital_tok.domain.ImageMapping;
import com.digital_tok.repository.ImageMappingRepository;
import com.digital_tok.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMappingRepository imageMappingRepository;

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

    public record PreviewResult(Long imageId, String previewUrl, LocalDateTime updatedAt) {}
    public record BinaryResult(Long imageId, String einkDataUrl, LocalDateTime lastUsedAt) {}
}
