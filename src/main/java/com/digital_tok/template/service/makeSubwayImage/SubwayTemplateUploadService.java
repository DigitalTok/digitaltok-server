package com.digital_tok.template.service.makeSubwayImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SubwayTemplateUploadService { // 이미지 생성 후 S3에 업로드

    private final Eink4ColorService imageGenerator;
    private final S3UploadService s3Uploader;
    private final SubwayTemplateService subwayTemplateService;

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

        // 3. DB 저장 (DB 작업 -> 트랜잭션 -> 별도 호출)
        return subwayTemplateService.saveToDatabase(nameKor, nameEng, lineName, uploadedImageUrl);
    }
}
