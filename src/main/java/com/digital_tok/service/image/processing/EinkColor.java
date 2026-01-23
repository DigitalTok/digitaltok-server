package com.digital_tok.service.image.processing;

public enum EinkColor {

    BLACK(0),
    WHITE(1),
    YELLOW(2),
    RED(3);

    private final int code;

    EinkColor(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
