package com.digital_tok.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON200_1", "요청이 정상적으로 처리되었습니다."),

    DEVICE_STATUS_SUCCESS(HttpStatus.OK, "DEVICE200", "기기 상태 조회 성공"),
    DEVICE_CONNECT_SUCCESS(HttpStatus.OK, "DEVICE201", "기기 연결 성공"),
    DEVICE_DISCONNECT_SUCCESS(HttpStatus.OK, "DEVICE202", "기기 연결 해제 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
