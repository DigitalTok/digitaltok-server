package com.digital_tok.image.handler;

import com.digital_tok.global.AmazonS3Manager;
import com.digital_tok.image.domain.ImageStatus;
import com.digital_tok.image.repository.ImageRepository;
import com.digital_tok.image.service.ImageDerivationService;
import com.digital_tok.image.service.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageProcessListener {

    private final AmazonS3Manager s3Manager;
    private final ImageDerivationService imageDerivationService;
    private final ImageUpdateService imageUpdateService;
    private final ImageRepository imageRepository;

    @Async // 사용자는 기다리지 않게 비동기 처리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImageProcess(ImageProcessEvent event) {
        Long imageId = event.imageId();

        try {
            // 1. S3 업로드 (기존 로직)
//            String originalUrl = s3Manager.uploadFile("images", event.getFile());
            String originalUrl = s3Manager.uploadFile("images", event.fileBytes(), event.contentType(), event.extension());
//             TODO: Argument 개수 수정필요, 오버로딩 하면 될듯?

            // 2. e-ink 파생 생성 (기존 fallback 정책 유지)
            ImageDerivationService.Result derived;
            try {
                derived = imageDerivationService.derive(new ByteArrayInputStream(event.fileBytes()));
            } catch (Exception e) {
                log.warn("Derive failed for imageId={}, fallback to original", imageId);
                derived = new ImageDerivationService.Result(null, null);
            }

            // 3. 최종 결과 DB 업데이트 (새로운 트랜잭션)
            imageUpdateService.updateImageStatus(imageId, originalUrl, derived, ImageStatus.COMPLETED);

        } catch (Exception e) {
            log.error("Critical error in image processing for imageId={}", imageId, e);
            imageUpdateService.updateImageStatus(imageId, null, null, ImageStatus.FAILED);
        }
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void updateImageStatus(Long id, String originalUrl, ImageDerivationService.Result derived, ImageStatus status) {
//        Image image = imageRepository.findById(id).orElseThrow();
//
//        // 기존 필드 업데이트 로직
//        String previewUrl = (derived.previewUrl() != null) ? derived.previewUrl() : originalUrl;
//
//        image.completeUpload(originalUrl, previewUrl, derived.einkDataUrl(), status);
//        imageRepository.save(image);
//    }


}
