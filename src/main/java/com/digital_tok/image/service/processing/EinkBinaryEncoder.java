//binary 파일 생성
package com.digital_tok.image.service.processing;

import java.awt.image.BufferedImage;

public class EinkBinaryEncoder {

    private final EinkEncodingOption option;

    public EinkBinaryEncoder(EinkEncodingOption option) {
        this.option = option;
    }

    /**
     * 입력 이미지(반드시 200x200, 4색으로 양자화된 상태 권장)를
     * e-ink용 2bpp packed binary로 변환한다.
     *
     * @param quantized200x200 200x200 이미지 (픽셀은 BLACK/WHITE/YELLOW/RED에 준하는 값이어야 함)
     * @return 10000 bytes (200*200*2bit)
     */
    public byte[] encode(BufferedImage quantized200x200) {
        int width = option.getWidth();
        int height = option.getHeight();

        if (quantized200x200.getWidth() != width || quantized200x200.getHeight() != height) {
            throw new IllegalArgumentException(
                    "Image must be " + width + "x" + height + " but was "
                            + quantized200x200.getWidth() + "x" + quantized200x200.getHeight()
            );
        }

        int totalPixels = width * height;
        int expectedBytes = (totalPixels * 2) / 8; // 2bpp
        if ((totalPixels * 2) % 8 != 0) {
            throw new IllegalStateException("Unexpected: pixel bits not aligned to byte boundary.");
        }

        byte[] out = new byte[expectedBytes];

        // 세로 스캔: x=0..width-1, y=0..height-1 순으로 픽셀을 읽는다.
        // 4픽셀(2bit*4=8bit)을 1바이트로 패킹:
        // byte = (p0<<6) | (p1<<4) | (p2<<2) | (p3)
        int outIndex = 0;

        if (option.getScanDirection() == EinkEncodingOption.ScanDirection.VERTICAL) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y += 4) {
                    int p0 = to2BitColorCode(quantized200x200.getRGB(x, y));
                    int p1 = to2BitColorCode(quantized200x200.getRGB(x, y + 1));
                    int p2 = to2BitColorCode(quantized200x200.getRGB(x, y + 2));
                    int p3 = to2BitColorCode(quantized200x200.getRGB(x, y + 3));

                    out[outIndex++] = pack4(p0, p1, p2, p3);
                }
            }
        } else {
            // 가로 스캔: y=0..height-1, x=0..width-1
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x += 4) {
                    int p0 = to2BitColorCode(quantized200x200.getRGB(x, y));
                    int p1 = to2BitColorCode(quantized200x200.getRGB(x + 1, y));
                    int p2 = to2BitColorCode(quantized200x200.getRGB(x + 2, y));
                    int p3 = to2BitColorCode(quantized200x200.getRGB(x + 3, y));

                    out[outIndex++] = pack4(p0, p1, p2, p3);
                }
            }
        }

        if (outIndex != out.length) {
            throw new IllegalStateException("Encoded length mismatch. wrote=" + outIndex + ", expected=" + out.length);
        }

        return out;
    }

    private static byte pack4(int p0, int p1, int p2, int p3) {
        // 각 픽셀은 0..3 (2bit)
        int b = ((p0 & 0b11) << 6)
                | ((p1 & 0b11) << 4)
                | ((p2 & 0b11) << 2)
                |  (p3 & 0b11);
        return (byte) (b & 0xFF);
    }

    /**
     * 양자화된 픽셀 RGB를 2bit 코드로 변환.
     * 지금 단계에서는 "정확히 4색으로 양자화된 이미지"가 들어온다는 전제 하에
     * 색에 가장 가까운 코드로 매핑한다.
     *
     * 코드 매핑(공급사 앱 기준):
     * 0: BLACK
     * 1: WHITE
     * 2: YELLOW
     * 3: RED
     */
    private static int to2BitColorCode(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb) & 0xFF;

        // 완전한 RGB 매칭을 기대하면 실무에서 미세 차이로 깨질 수 있어서,
        // "가장 가까운 팔레트"로 결정한다.
        // 팔레트(대략):
        // BLACK  = (0,0,0)
        // WHITE  = (255,255,255)
        // YELLOW = (255,255,0)
        // RED    = (255,0,0)

        int dBlack = dist2(r, g, b, 0, 0, 0);
        int dWhite = dist2(r, g, b, 255, 255, 255);
        int dYellow = dist2(r, g, b, 255, 255, 0);
        int dRed = dist2(r, g, b, 255, 0, 0);

        int min = dBlack;
        int code = EinkColor.BLACK.getCode();

        if (dWhite < min) { min = dWhite; code = EinkColor.WHITE.getCode(); }
        if (dYellow < min) { min = dYellow; code = EinkColor.YELLOW.getCode(); }
        if (dRed < min) { /*min = dRed;*/ code = EinkColor.RED.getCode(); }

        return code;
    }

    private static int dist2(int r1, int g1, int b1, int r2, int g2, int b2) {
        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;
        return dr * dr + dg * dg + db * db;
    }
}
