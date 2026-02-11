package com.digital_tok.global.event;

import com.digital_tok.template.service.makeSubwayImage.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3RollbackEventHandler {

    private final S3UploadService s3UploadService;

    @Async // 별도 스레드에서 실행
    @EventListener
    public void handleRollback(S3ImageRollbackEvent event) {
        String fileUrl = event.fileUrl();
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            log.info("[Rollback] S3 파일 삭제 시도: {}", fileUrl);
            s3UploadService.deleteFile(fileUrl);
            log.info("[Rollback] S3 파일 삭제 성공");
        } catch (Exception e) {
            // 삭제 실패 시 로그를 남겨서 나중에 수동으로라도 지울 수 있게 해야 함
            log.error("[Rollback] S3 파일 삭제 실패 (수동 확인 필요). URL: {}, Error: {}", fileUrl, e.getMessage());
        }
    }
}
