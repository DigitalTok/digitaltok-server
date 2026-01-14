package com.digital_tok.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON200_1", "요청이 정상적으로 처리되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
