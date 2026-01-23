//png preview 생성
package com.digital_tok.service.image.processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EinkPreviewRenderer {

    /**
     * e-ink 출력 시뮬레이션 이미지를 PNG 바이트로 변환한다.
     * 입력 이미지는 반드시 200x200, 4색(검/흰/노/빨)로 양자화된 상태여야 한다.
     */
    public byte[] renderPng(BufferedImage einkPreviewImage) {
        if (einkPreviewImage == null) {
            throw new IllegalArgumentException("preview image is null");
        }

        if (einkPreviewImage.getWidth() != 200 || einkPreviewImage.getHeight() != 200) {
            throw new IllegalArgumentException(
                    "Preview image must be 200x200 but was "
                            + einkPreviewImage.getWidth() + "x" + einkPreviewImage.getHeight()
            );
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(einkPreviewImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to render preview PNG", e);
        }
    }
}
