//기기 스펙
package com.digital_tok.image.service.processing;

public class EinkEncodingOption {

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    public enum ScanDirection {
        VERTICAL,
        HORIZONTAL
    }

    private final int width;
    private final int height;
    private final ScanDirection scanDirection;

    public EinkEncodingOption() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.scanDirection = ScanDirection.HORIZONTAL;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ScanDirection getScanDirection() {
        return scanDirection;
    }
}
