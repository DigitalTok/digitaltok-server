package com.digital_tok.image.handler;


/**
 * 이미지 처리를 위한 이벤트 객체
 * @param imageId      DB에 저장된 Image 엔티티의 ID
 * @param fileBytes    파일의 바이너리 데이터 (비동기 처리를 위해 메모리에 보관)
 * @param contentType  파일의 확장자/타입 (S3 업로드 시 필요)
 */
public record ImageProcessEvent(
        Long imageId,
        byte[] fileBytes,
        String contentType,
        String extension
) {}
