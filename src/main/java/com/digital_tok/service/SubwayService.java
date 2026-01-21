package com.digital_tok.service;

import com.digital_tok.domain.SubwayTemplate;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.repository.SubwayTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayService {

    private final SubwayTemplateRepository subwayTemplateRepository;

    // 지하철역 목록을 오름차순으로 반환
    public List<SubwayTemplate> getSubwayTemplates() {
        return subwayTemplateRepository.findAllByOrderByStationNameAsc();
    }

    public SubwayTemplate getSubwayTemplate(Long templateId) {
        return subwayTemplateRepository.findById(templateId)
                .orElseThrow(() -> new GeneralException(ErrorCode.STATION_NOT_FOUND));
    }
}
