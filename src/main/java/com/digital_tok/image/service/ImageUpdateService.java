package com.digital_tok.image.service;

import com.digital_tok.image.domain.Image;
import com.digital_tok.image.domain.ImageStatus;
import com.digital_tok.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageUpdateService {

    private final ImageRepository imageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImageStatus(Long id, String originalUrl, ImageDerivationService.Result derived, ImageStatus status) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found: " + id));

        // 파생 데이터가 있으면 사용하고, 없으면 원본 URL을 프리뷰로 사용 (Fallback 정책)
        String previewUrl = (derived != null && derived.previewUrl() != null)
                ? derived.previewUrl()
                : originalUrl;

        String einkDataUrl = (derived != null) ? derived.einkDataUrl() : null;

        image.completeUpload(originalUrl, previewUrl, einkDataUrl, status);
    }
}
