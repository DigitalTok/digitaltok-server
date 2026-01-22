package com.digital_tok.service.storage;

import com.digital_tok.service.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final AmazonS3Manager amazonS3Manager;

    @Override
    public String uploadPreview(byte[] bytes, String key, String contentType) {
        MultipartFile file = new ByteArrayMultipartFile(bytes, key, contentType);
        return amazonS3Manager.uploadFile("preview", file);
    }

    @Override
    public String uploadEink(byte[] bytes, String key, String contentType) {
        MultipartFile file = new ByteArrayMultipartFile(bytes, key, contentType);
        return amazonS3Manager.uploadFile("eink", file);
    }
}
