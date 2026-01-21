package com.digital_tok.convertor;

import com.digital_tok.domain.SubwayTemplate;
import com.digital_tok.dto.response.SubwayResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayConvertor {

    // 단일 역 Entity to DTO 변환
    public SubwayResponseDTO.SubwayItemDto toItemDto(SubwayTemplate entity) {
        return SubwayResponseDTO.SubwayItemDto.builder()
                .subwayTemplateId(entity.getSubwayTemplateId())
                .stationName(entity.getStationName())
                .lineName(entity.getLineName())
                .previewUrl(entity.getTemplateImageUrl())
                .build();
    }

    // 리스트 Entity to DTO 변환
    public SubwayResponseDTO.SubwayListDto toListDto(List<SubwayTemplate> entities) {
        List<SubwayResponseDTO.SubwayItemDto> items = entities.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());

        return SubwayResponseDTO.SubwayListDto.builder()
                .count(items.size())
                .items(items)
                .build();
    }
}
