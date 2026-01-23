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
import com.digital_tok.service.image.ImageDerivationService;

import java.time.LocalDateTime;

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
     */
    public UploadResult uploadImage(MultipartFile file, String imageName, Long userId) {

        byte[] originalBytes;
        try {
            originalBytes = file.getBytes();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read uploaded file bytes", e);
        }

        // 1) S3 업로드 (원본)
        // ⚠️ 가능하면 s3Manager도 bytes 업로드 버전이 있으면 그걸 쓰는 게 제일 좋음
        String originalUrl = s3Manager.uploadFile("images", file);

        // 2) e-ink 파생 생성
        ImageDerivationService.Result derived;
        try {
            System.out.println("### DERIVE START ###");
            derived = imageDerivationService.derive(new java.io.ByteArrayInputStream(originalBytes));
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
                .lastUsedAt(null)
                .build();

        imageMappingRepository.save(mapping);

        return new UploadResult(image, mapping);
    }

    public PreviewResult getPreview(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("image not found: " + imageId));

        // previewUrl 없으면(예전 데이터) fallback으로 originalUrl이라도 반환
        String url = (image.getPreviewUrl() != null && !image.getPreviewUrl().isBlank())
                ? image.getPreviewUrl()
                : image.getOriginalUrl();

        return new PreviewResult(image.getImageId(), url, LocalDateTime.now());
    }

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
            BinaryResult br = new BinaryResult(image.getImageId(), image.getEinkDataUrl(), now);
            System.out.println("### SERVICE br.class=" + br.getClass());
            System.out.println("### SERVICE br=" + br);
            System.out.println("### SERVICE br.hash=" + System.identityHashCode(br));
            return br;
        }

        BinaryResult br = new BinaryResult(image.getImageId(), null, now);
        System.out.println("### SERVICE br=" + br);
        System.out.println("### SERVICE br.hash=" + System.identityHashCode(br));
        return br;

    }

    // 반환용 record
    public record UploadResult(Image image, ImageMapping mapping) {}
    public record PreviewResult(Long imageId, String previewUrl, LocalDateTime updatedAt) {}
    public record BinaryResult(Long imageId, String einkDataUrl, LocalDateTime lastUsedAt) {}
}
