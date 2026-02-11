package com.digital_tok.template.service.makeSubwayImage;

import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
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

    /**
     * S3 파일 삭제 메서드 (롤백용)
     * @param fileUrl S3 전체 URL (예: https://bucket.s3.region.amazonaws.com/folder/file.png)
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            // 1. URL에서 Key 추출 ,도메인 제외하고 경로만 가져옴
            URI uri = new URI(fileUrl);
            String key = uri.getPath();

            // Key 앞의 '/' 제거 (예: /folder/file.png -> folder/file.png)
            if (key.startsWith("/")) {
                key = key.substring(1);
            }

            // 2. 삭제 요청 생성
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            // 3. 삭제 실행
            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3 파일 삭제 완료: key={}", key);

        } catch (Exception e) {
            // 삭제 실패 시 에러를 던져서 리스너가 알 수 있게 함 (로그는 리스너에서도 찍음)
            log.error("S3 파일 삭제 실패: url={}", fileUrl, e);
            throw new GeneralException(ErrorCode.TEMPLATE_DELETE_FAIL);
        }
    }
}
