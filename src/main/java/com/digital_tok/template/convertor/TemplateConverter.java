package com.digital_tok.template.convertor;

import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.template.dto.TemplateResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateConverter {

    // 단일 역 Entity to DTO 변환
    public TemplateResponseDTO.SubwayItemDto toSubwayItemDto(SubwayTemplate entity) {
        return TemplateResponseDTO.SubwayItemDto.builder()
                .templateId(entity.getId())
                .stationName(entity.getStationName())
                .lineName(entity.getLineName())
                .templateImageUrl(entity.getTemplateImageUrl())
                .build();
    }

    // 리스트 Entity to DTO 변환
    public TemplateResponseDTO.SubwayListDto toSubwayListDto(List<SubwayTemplate> entities) {
        List<TemplateResponseDTO.SubwayItemDto> items = entities.stream()
                .map(this::toSubwayItemDto)
                .collect(Collectors.toList());

        return TemplateResponseDTO.SubwayListDto.builder()
                .count(items.size())
                .items(items)
                .build();
    }

    public TemplateResponseDTO.SubwayDetailDto toSubwayDetailDto(SubwayTemplate entity) {
        return TemplateResponseDTO.SubwayDetailDto.builder()
                .templateId(entity.getId())
                .stationName(entity.getStationName())
                .stationNameEng(entity.getStationNameEng())
                .lineName(entity.getLineName())
                .templateImageUrl(entity.getTemplateImageUrl())
                .templateDataUrl(entity.getTemplateDataUrl())
                .build();
    }
    
    // TODO: 교통약자 템플릿 관련 기능 추가
}
