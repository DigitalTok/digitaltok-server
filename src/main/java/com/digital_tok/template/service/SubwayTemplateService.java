package com.digital_tok.template.service;

import com.digital_tok.template.domain.SubwayTemplate;
import com.digital_tok.template.repository.SubwayTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SubwayTemplateService { // 지하철 이미지 템플릿 생성 관련 서비스

    private final Eink4ColorService imageGenerator;
    private final S3UploadService s3Uploader;
    private final SubwayTemplateRepository subwayTemplateRepository;

    public Long createAndSaveSubwayTemplate(String nameKor, String nameEng, String lineName) {

        // 1. 이미지 생성
        byte[] imageBytes;
        try {
            imageBytes = imageGenerator.generatePatternImage(nameKor, nameEng, lineName);
        } catch (IOException e) {
            throw new RuntimeException("이미지 생성 오류", e);
        }

        // 2. S3 업로드
        String uploadedImageUrl = s3Uploader.upload(imageBytes, "template/subway");

        // 3. DB 저장 (DB 작업 - 트랜잭션 O) -> 별도 메서드로 호출
        return saveToDatabase(nameKor, nameEng, lineName, uploadedImageUrl);
    }

    @Transactional // 여기서부터 트랜잭션 시작
    protected Long saveToDatabase(String nameKor, String nameEng, String lineName, String imageUrl) {

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
