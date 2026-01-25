//ImageService랑 S3 연결
package com.digital_tok.service.storage;

public interface StorageService {

    /**
     * 미리보기 이미지 업로드 후 접근 가능한 URL 반환
     */
    String uploadPreview(byte[] bytes, String key, String contentType);

    /**
     * E-ink 바이너리 업로드 후 접근 가능한 URL 반환
     */
    String uploadEink(byte[] bytes, String key, String contentType);
}
