//원본->200*200 사이즈 변환
package com.digital_tok.service.image.processing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class EinkImagePreprocessor {

    private final EinkEncodingOption option;

    public EinkImagePreprocessor(EinkEncodingOption option) {
        this.option = option;
    }

    /**
     * 원본 이미지를 "비율 유지 + 센터 크롭"으로 정사각형에 맞춘 뒤,
     * 최종적으로 200x200으로 리사이즈한다.
     */
    public BufferedImage to200x200(BufferedImage src) {
        int targetW = option.getWidth();
        int targetH = option.getHeight();

        if (src == null) {
            throw new IllegalArgumentException("src image is null");
        }

        // 1) 센터 크롭 영역 계산 (원본에서 정사각형 crop)
        int srcW = src.getWidth();
        int srcH = src.getHeight();
        if (srcW <= 0 || srcH <= 0) {
            throw new IllegalArgumentException("Invalid image size: " + srcW + "x" + srcH);
        }

        int cropSize = Math.min(srcW, srcH);
        int cropX = (srcW - cropSize) / 2;
        int cropY = (srcH - cropSize) / 2;

        BufferedImage cropped = src.getSubimage(cropX, cropY, cropSize, cropSize);

        // 2) 크롭된 정사각형을 200x200으로 고품질 리사이즈
        // e-ink에서는 경계/계단이 중요해서 렌더링 힌트를 확실히 줌.
        BufferedImage resized = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(cropped, 0, 0, targetW, targetH, null);
        } finally {
            g.dispose();
        }

        return resized;
    }
}
