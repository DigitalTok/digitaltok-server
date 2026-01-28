package com.digital_tok.template.service;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import com.digital_tok.template.domain.PriorityTemplate;
import com.digital_tok.template.repository.PriorityTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriorityService {

    private final PriorityTemplateRepository priorityTemplateRepository;

    // 교통약자 템플릿 목록 조회
    public List<PriorityTemplate> getPrioirtyTemplates() {
        return priorityTemplateRepository.findAll();
    }
    
    // 교통약자 템플릿 상세 조회
    public PriorityTemplate getPrioirtyTemplate(Long id) {
        return priorityTemplateRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.TEMPLATE_NOT_FOUND));
    }
    
    // 교통약자 템플릿 검색 기능 -> 보류
}
