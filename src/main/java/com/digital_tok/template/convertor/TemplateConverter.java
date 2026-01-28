package com.digital_tok.template.convertor;

import com.digital_tok.template.domain.PriorityTemplate;
import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.template.dto.PriorityResponseDTO;
import com.digital_tok.template.dto.SubwayResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateConverter {

    // 단일 역 Entity to DTO 변환
    public SubwayResponseDTO.SubwayItemDto toSubwayItemDto(SubwayTemplate entity) {
        return SubwayResponseDTO.SubwayItemDto.builder()
                .templateId(entity.getId())
                .stationName(entity.getStationName())
                .lineName(entity.getLineName())
                .templateImageUrl(entity.getTemplateImageUrl())
                .build();
    }

    // 리스트 Entity to DTO 변환
    public SubwayResponseDTO.SubwayListDto toSubwayListDto(List<SubwayTemplate> entities) {
        List<SubwayResponseDTO.SubwayItemDto> items = entities.stream()
                .map(this::toSubwayItemDto)
                .collect(Collectors.toList());

        return SubwayResponseDTO.SubwayListDto.builder()
                .count(items.size())
                .items(items)
                .build();
    }

    public SubwayResponseDTO.SubwayDetailDto toSubwayDetailDto(SubwayTemplate entity) {
        return SubwayResponseDTO.SubwayDetailDto.builder()
                .templateId(entity.getId())
                .stationName(entity.getStationName())
                .stationNameEng(entity.getStationNameEng())
                .lineName(entity.getLineName())
                .templateImageUrl(entity.getTemplateImageUrl())
                .templateDataUrl(entity.getTemplateDataUrl())
                .build();
    }


    // 교통약자 템플릿 Entity to DTO 변환
    public PriorityResponseDTO.PriorityItemDto toPriorityItemDto(PriorityTemplate entity) {
        return PriorityResponseDTO.PriorityItemDto.builder()
                .templateId(entity.getId())
                .priorityType(entity.getPriorityType())
                .templateImageUrl(entity.getTemplateImageUrl())
                .build();
    }

    public PriorityResponseDTO.PriorityListDto toPriorityListDto(List<PriorityTemplate> entities) {
        List<PriorityResponseDTO.PriorityItemDto> items = entities.stream()
                .map(this::toPriorityItemDto)
                .toList();

        return PriorityResponseDTO.PriorityListDto.builder()
                .count(entities.size())
                .items(items)
                .build();
    }

    // 교통약자 템플릿 상세 정보 Entity to DTO 변환
    public PriorityResponseDTO.PriorityDetailDto toPriorityDetailDto(PriorityTemplate entity) {
        return PriorityResponseDTO.PriorityDetailDto.builder()
                .templateId(entity.getId())
                .priority_type(entity.getPriorityType())
                .templateImageUrl(entity.getTemplateImageUrl())
                .templateDataUrl(entity.getTemplateDataUrl())
                .build();
    }
}
