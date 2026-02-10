//원본->4색 변환
package com.digital_tok.image.service.processing;

import java.awt.image.BufferedImage;

public class EinkQuantizer {

    /**
     * 디더링 사용 여부.
     * - OFF: 단순 최근접색(빠르고 안정적)
     * - ON : Floyd–Steinberg 디더링(사진에서 더 'e-ink 느낌', 대신 노이즈 감)
     */
    public enum DitherMode {
        OFF,
        FLOYD_STEINBERG
    }

    // 팔레트 RGB (기기 4색 느낌에 맞춘 기본값)
    private static final int[] BLACK = {0, 0, 0};
    private static final int[] WHITE = {255, 255, 255};
    private static final int[] YELLOW = {255, 255, 0};
    private static final int[] RED = {255, 0, 0};

    /**
     * 입력(200x200 권장)을 4색으로 양자화한 결과를 반환한다.
     * 출력 픽셀은 팔레트 중 하나의 RGB로 "정확히" 떨어진다.
     */
    public BufferedImage quantizeTo4Colors(BufferedImage src, DitherMode ditherMode) {
        if (src == null) throw new IllegalArgumentException("src image is null");

        int w = src.getWidth();
        int h = src.getHeight();

        // 디더링을 위해 float 버퍼로 작업(오차 누적)
        float[][][] buf = new float[h][w][3];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                buf[y][x][0] = (argb >> 16) & 0xFF;
                buf[y][x][1] = (argb >> 8) & 0xFF;
                buf[y][x][2] = (argb) & 0xFF;
            }
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int oldR = clamp(Math.round(buf[y][x][0]));
                int oldG = clamp(Math.round(buf[y][x][1]));
                int oldB = clamp(Math.round(buf[y][x][2]));

                // Blue 보정 적용 (팔레트 고르기 전)
                boolean blue = isBlueHue(oldR, oldG, oldB);
                if (blue) {
                    int boosted = boostBlueValue(oldR, oldG, oldB); // 0xFFRRGGBB 형태
                    oldR = (boosted >> 16) & 0xFF;
                    oldG = (boosted >> 8) & 0xFF;
                    oldB = boosted & 0xFF;
                }

                int[] nearest = nearestPalette(oldR, oldG, oldB, blue);

                // 결과 픽셀 고정(팔레트 색으로)
                int newR = nearest[0];
                int newG = nearest[1];
                int newB = nearest[2];
                int outArgb = (0xFF << 24) | (newR << 16) | (newG << 8) | newB;
                out.setRGB(x, y, outArgb);

                if (ditherMode == DitherMode.FLOYD_STEINBERG) {
                    // 양자화 오차 계산
                    float errR = oldR - newR;
                    float errG = oldG - newG;
                    float errB = oldB - newB;

                    // Floyd–Steinberg 확산
                    //        x     7/16
                    // 3/16  5/16  1/16  (다음 줄)
                    diffuse(buf, x + 1, y,     errR, errG, errB, 7f / 16f);
                    diffuse(buf, x - 1, y + 1, errR, errG, errB, 3f / 16f);
                    diffuse(buf, x,     y + 1, errR, errG, errB, 5f / 16f);
                    diffuse(buf, x + 1, y + 1, errR, errG, errB, 1f / 16f);
                }
            }
        }

        return out;
    }

    private static void diffuse(float[][][] buf, int x, int y, float errR, float errG, float errB, float factor) {
        int h = buf.length;
        int w = buf[0].length;
        if (x < 0 || y < 0 || x >= w || y >= h) return;

        buf[y][x][0] = clampFloat(buf[y][x][0] + errR * factor);
        buf[y][x][1] = clampFloat(buf[y][x][1] + errG * factor);
        buf[y][x][2] = clampFloat(buf[y][x][2] + errB * factor);
    }

    private static int[] nearestPalette(int r, int g, int b, boolean blue) {
        int dBlack = dist2(r, g, b, BLACK[0], BLACK[1], BLACK[2]);
        int dWhite = dist2(r, g, b, WHITE[0], WHITE[1], WHITE[2]);
        int dYellow = dist2(r, g, b, YELLOW[0], YELLOW[1], YELLOW[2]);
        int dRed = dist2(r, g, b, RED[0], RED[1], RED[2]);

        // Blue 계열이 RED/YELLOW로 점이 튀는게 가장 어색함 → 거리 penalty로 억제
        if (blue) {
            dRed = (int)(dRed * 1.35);
            dYellow = (int)(dYellow * 1.20);
        }

        int min = dBlack;
        int[] best = BLACK;

        if (dWhite < min) { min = dWhite; best = WHITE; }
        if (dYellow < min) { min = dYellow; best = YELLOW; }
        if (dRed < min) { min = dRed; best = RED; }

        return best;
    }


    private static int dist2(int r1, int g1, int b1, int r2, int g2, int b2) {
        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;
        return dr * dr + dg * dg + db * db;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private static float clampFloat(float v) {
        return Math.max(0f, Math.min(255f, v));
    }
    // EinkQuantizer.java 내부에 추가
    private static boolean isBlueHue(int r, int g, int b) {
        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        float h = hsb[0] * 360f;   // hue: 0~360
        float s = hsb[1];          // saturation: 0~1
        float v = hsb[2];          // value/brightness: 0~1

        // 파랑/청록 계열(대략 180~260도), 채도 어느 정도 있는 경우만
        return (h >= 180f && h <= 260f) && (s >= 0.25f) && (v >= 0.10f);
    }

    private static int boostBlueValue(int r, int g, int b) {
        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        float h = hsb[0];
        float s = hsb[1];
        float v = hsb[2];

        // Blue 영역은 밝기(v)를 살짝 올려서 "죽은 회색 덩어리"를 방지
        // 너무 올리면 하얗게 날아가니까 1.15~1.35 사이에서 시작 추천
        float boostedV = Math.min(1.0f, v * 1.35f);

        int rgb = java.awt.Color.HSBtoRGB(h, s, boostedV);
        return rgb; // int packed RGB
    }

}
