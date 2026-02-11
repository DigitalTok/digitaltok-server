package com.digital_tok.template.service.makeSubwayImage;

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
public class S3UploadService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

//    // 기존 메서드 (내부에서 범용 메서드 호출하도록 변경)
//    public String upload(byte[] imageBytes, String directoryPath) {
//        return upload(imageBytes, directoryPath, "png", "image/png");
//    }

    // 확장자와 Content-Type을 지정할 수 있는 범용 업로드 메서드
    public String upload(byte[] fileBytes, String directoryPath, String extension, String contentType) {
        // 1. 파일명 생성
        String fileName = directoryPath + "/" + UUID.randomUUID() + "." + extension;

        // 2. 업로드 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(contentType)
                .contentLength((long) fileBytes.length)
                .build();

        // 3. 업로드 실행
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패: " + fileName, e);
        }

        // 4. URL 반환
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build()).toExternalForm();
    }
}
