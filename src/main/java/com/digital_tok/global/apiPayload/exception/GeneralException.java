package com.digital_tok.global.apiPayload.exception;

import com.digital_tok.global.apiPayload.code.BaseErrorCode;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public GeneralException(BaseErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모(RuntimeException)에게 에러 메시지를 넘겨줌
        this.errorCode = errorCode;
    }
}
