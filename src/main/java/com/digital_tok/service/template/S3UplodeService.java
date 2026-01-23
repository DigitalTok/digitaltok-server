package com.digital_tok.service.template;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3UplodeService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(byte[] imageBytes, String directoryPath) {
        // 1. 파일명 생성
        String fileName = directoryPath + "/" + UUID.randomUUID() + ".png";

        // 2. 업로드 요청 생성 (Builder 패턴 사용)
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType("image/png")
                .contentLength((long) imageBytes.length)
                .build();

        // 3. 업로드 실행
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        // 4. URL 반환 (V2 방식)
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build()).toExternalForm();
    }
}
