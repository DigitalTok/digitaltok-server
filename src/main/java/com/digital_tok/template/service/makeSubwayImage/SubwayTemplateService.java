package com.digital_tok.template.service.makeSubwayImage;

import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.template.repository.SubwayTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class SubwayTemplateService { // 생성된 지하철 이미지를 받아서 DB에 저장
    private final SubwayTemplateRepository subwayTemplateRepository;
    public Long saveToDatabase(String nameKor, String nameEng, String lineName, String imageUrl) {

        SubwayTemplate subwayTemplate = SubwayTemplate.builder()
                // 부모(Template) 필드
                .templateImageUrl(imageUrl)
                .templateDataUrl(null) //TODO: 바이너리 데이터 변환 로직 완성 후 null값 대체해야함.

                // 자식(SubwayTemplate) 필드
                .stationName(nameKor)
                .stationNameEng(nameEng)
                .lineName(lineName + "호선")
                .build();

        return subwayTemplateRepository.save(subwayTemplate).getId();
    }
}
