package com.digital_tok.image.service.storage;

import com.digital_tok.global.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

//@Profile("prod")
@Primary
@ConditionalOnProperty(name = "storage.type", havingValue = "s3", matchIfMissing = true)
@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final AmazonS3Manager amazonS3Manager;

    @Override
    public String uploadPreview(byte[] bytes, String key, String contentType) {
        return amazonS3Manager.uploadBytes(key, bytes, contentType);
    }

    @Override
    public String uploadEink(byte[] bytes, String key, String contentType) {
        return amazonS3Manager.uploadBytes(key, bytes, contentType);
    }
}

