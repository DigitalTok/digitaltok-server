package com.digital_tok.template.service;

import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.template.repository.SubwayTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayService { // 지하철 관련 API 호출 서비스

    private final SubwayTemplateRepository subwayTemplateRepository;

    // 지하철역 목록을 오름차순으로 반환
    public List<SubwayTemplate> getSubwayTemplates() {
        return subwayTemplateRepository.findAllByOrderByStationNameAsc();
    }

    public SubwayTemplate getSubwayTemplate(Long templateId) {
        return subwayTemplateRepository.findById(templateId)
                .orElseThrow(() -> new GeneralException(ErrorCode.STATION_NOT_FOUND));
    }

    // 지하철 역 검색 서비스
    public List<SubwayTemplate> searchSubwayTemplates(String keyword) {
        // 1. 키워드가 없거나 공백이면 -> 전체 목록 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return subwayTemplateRepository.findAllByOrderByStationNameAsc();
        }

        // 2. 키워드가 있으면 검색 (한글 OR 영어)
        return subwayTemplateRepository.findByStationNameContainingOrStationNameEngContainingIgnoreCase(keyword, keyword);
    }
}
