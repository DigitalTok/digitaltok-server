package com.digital_tok.service;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import io.awspring.cloud.s3.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Manager {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String keyName, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = (fileName != null && fileName.contains("."))
                ? fileName.substring(fileName.lastIndexOf("."))
                : "";

        String s3Key = keyName + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build()).toExternalForm();

        } catch (IOException e) {
            log.error("이미지 업로드 중 에러 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }
    // preview/bin 파생 업로드용 (새로 추가)
    public String uploadBytes(String s3Key, byte[] bytes, String contentType) {
        try {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(req, RequestBody.fromBytes(bytes));

            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build()).toExternalForm();

        } catch (Exception e) {
            log.error("바이너리 업로드 중 에러 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
}
}
