//바이트 배열을 multipartfile로 감싸는 래퍼
package com.digital_tok.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayMultipartFile implements MultipartFile {

    private final byte[] content;
    private final String name;
    private final String contentType;

    public ByteArrayMultipartFile(byte[] content, String name, String contentType) {
        this.content = content;
        this.name = name;
        this.contentType = contentType;
    }

    @Override public String getName() { return name; }
    @Override public String getOriginalFilename() { return name; }
    @Override public String getContentType() { return contentType; }
    @Override public boolean isEmpty() { return content == null || content.length == 0; }
    @Override public long getSize() { return content.length; }
    @Override public byte[] getBytes() { return content; }
    @Override public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(content); }
    @Override public void transferTo(java.io.File dest) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }
}
