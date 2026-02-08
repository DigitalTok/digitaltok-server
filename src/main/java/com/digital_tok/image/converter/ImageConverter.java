package com.digital_tok.image.converter;

import com.digital_tok.image.domain.ImageMapping;
import com.digital_tok.image.dto.ImageResponseDTO;
import com.digital_tok.image.service.ImageService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ImageConverter {

    /**
     * uploadImage 응답 변환
     */
    public ImageResponseDTO.UploadResultDto toUploadResultDto(ImageService.UploadResult r) {

        ImageResponseDTO.UploadedImageDto imageDto = ImageResponseDTO.UploadedImageDto.builder()
                .imageId(r.image().getImageId())
                .originalUrl(r.image().getOriginalUrl())
                .previewUrl(r.image().getPreviewUrl())
                .einkDataUrl(r.image().getEinkDataUrl())
                .imageName(r.image().getImageName())
                .createdAt(r.image().getCreatedAt())
                .deletedAt(r.image().getDeletedAt())
                .build();

        ImageResponseDTO.UploadedImageMappingDto mappingDto = ImageResponseDTO.UploadedImageMappingDto.builder()
                .userImageId(r.mapping().getUserImageId())
                .userId(r.mapping().getUserId())
                .imageId(r.image().getImageId())
                .isFavorite(r.mapping().getIsFavorite())
                .savedAt(r.mapping().getSavedAt())
                .lastUsedAt(r.mapping().getLastUsedAt())
                .build();

        return ImageResponseDTO.UploadResultDto.builder()
                .image(imageDto)
                .imageMapping(mappingDto)
                .build();
    }

    /**
     * preview 응답 변환
     */
    public ImageResponseDTO.PreviewResultDto toPreviewResultDto(ImageService.PreviewResult r) {
        return ImageResponseDTO.PreviewResultDto.builder()
                .imageId(r.imageId())
                .previewUrl(r.previewUrl())
                .updatedAt(r.updatedAt())
                .build();
    }

    /**
     * binary 응답 변환 (meta 포함)
     */
    public ImageResponseDTO.BinaryResultDto toBinaryResultDto(ImageService.BinaryResult r) {
        return ImageResponseDTO.BinaryResultDto.builder()
                .imageId(r.imageId())
                .einkDataUrl(r.einkDataUrl())
                .lastUsedAt(r.lastUsedAt())
                .meta(defaultBinaryMeta())
                .build();
    }

    private ImageResponseDTO.BinaryResultDto.MetaDto defaultBinaryMeta() {
        return ImageResponseDTO.BinaryResultDto.MetaDto.builder()
                .width(200)
                .height(200)
                .bpp(2)
                .palette("Black=0, White=1, Yellow=2, Red=3")
                .packing("MSB-first")
                .scan("row-major")
                .payloadBytes(10000)
                .hasHeader(false)
                .build();
    }

    /**
     * recent 목록 응답 변환 (Entity/List -> DTO)
     */
    public ImageResponseDTO.RecentImageListDto toRecentImageListDto(List<ImageMapping> mappings) {

        List<ImageResponseDTO.RecentImageDto> items = mappings.stream()
                .map(m -> ImageResponseDTO.RecentImageDto.builder()
                        .imageId(m.getImage().getImageId())
                        .previewUrl(m.getImage().getPreviewUrl())
                        .imageName(m.getImage().getImageName())
                        .isFavorite(m.getIsFavorite())
                        .lastUsedAt(m.getLastUsedAt())
                        .build())
                .toList();

        return ImageResponseDTO.RecentImageListDto.builder()
                .count(items.size())
                .items(items)
                .build();
    }

    /**
     * favorite 응답 변환
     */
    public ImageResponseDTO.FavoriteResultDto toFavoriteResultDto(
            Long userId, Long imageId, Boolean isFavorite, LocalDateTime updatedAt
    ) {
        return ImageResponseDTO.FavoriteResultDto.builder()
                .userId(userId)
                .imageId(imageId)
                .isFavorite(isFavorite)
                .favoriteCount(null) // 아직 계산 로직 없으면 null 유지
                .updatedAt(updatedAt)
                .build();
    }
}
