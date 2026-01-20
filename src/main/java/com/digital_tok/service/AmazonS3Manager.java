package com.digital_tok.service;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Manager {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String keyName, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // 확장자 추출 로직
        String extension = fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf("."))
                : "";

        // S3에 저장될 최종 파일 이름 생성
        String s3FileName = keyName
                + "/"
                + UUID.randomUUID().toString() + extension;

        // 2. S3 업로드 실행
        try {
            // 메타데이터 설정 (파일 타입 지정)
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            // s3Template이 실제로 업로드를 수행
            s3Template.upload(bucket, s3FileName, file.getInputStream(), metadata);

            // 3. 업로드된 이미지의 접근 URL 반환
            return s3Template.download(bucket, s3FileName).getURL().toString();

        } catch (IOException e) {
            log.error("이미지 업로드 중 에러 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }

    }
}
