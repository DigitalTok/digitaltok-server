package com.digital_tok.image.converter;

import com.digital_tok.image.domain.ImageMapping;
import com.digital_tok.image.dto.ImageResponseDTO;
import com.digital_tok.image.service.ImageService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ImageConverter {

    // uploadImage 응답 변환
    public ImageResponseDTO.UploadResultDto toUploadResultDto(ImageService.UploadResult r) {
        // TODO: implement
        return null;
    }

    // preview 응답 변환
    public ImageResponseDTO.PreviewResultDto toPreviewResultDto(ImageService.PreviewResult r) {
        // TODO: implement
        return null;
    }

    // binary 응답 변환 (meta 포함)
    public ImageResponseDTO.BinaryResultDto toBinaryResultDto(ImageService.BinaryResult r) {
        // TODO: implement
        return null;
    }

    // recent 목록 응답 변환
    public ImageResponseDTO.RecentImageListDto toRecentImageListDto(List<ImageMapping> mappings) {
        // TODO: implement
        return null;
    }

    // favorite 응답 변환
    public ImageResponseDTO.FavoriteResultDto toFavoriteResultDto(
            Long userId, Long imageId, Boolean isFavorite, LocalDateTime updatedAt
    ) {
        // TODO: implement
        return null;
    }
}
