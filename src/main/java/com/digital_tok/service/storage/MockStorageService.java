package com.digital_tok.service.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

//@Profile("dev")
@ConditionalOnProperty(name = "storage.type", havingValue = "mock")
@Service
public class MockStorageService implements StorageService {

    @Override
    public String uploadPreview(byte[] bytes, String key, String contentType) {
        // 실제 업로드 없이, URL처럼 보이는 문자열만 반환
        return "https://mock-storage.local/" + key;
    }

    @Override
    public String uploadEink(byte[] bytes, String key, String contentType) {
        return "https://mock-storage.local/" + key;
    }
}
