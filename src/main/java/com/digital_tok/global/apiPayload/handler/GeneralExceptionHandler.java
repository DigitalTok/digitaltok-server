package com.digital_tok.global.apiPayload.handler;

import com.digital_tok.global.apiPayload.ApiResponse;
import com.digital_tok.global.apiPayload.code.BaseErrorCode;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import com.digital_tok.global.apiPayload.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GeneralExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(GeneralException ex) {

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(ApiResponse.onFailure(ex.getErrorCode(), null));
    }

    // @Valid 검증 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        // 에러가 발생한 필드명과 메시지를 Map에 담음
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(ErrorCode.BAD_REQUEST, errors));
    }

    // 커스텀 예외에 포함되지 않는 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex, HttpServletRequest request) {

        // 오류난 url과 httpMethod 가져오고
        String url = request.getRequestURI();
        String method = request.getMethod();

        // 로그 출력
        log.error("[500 ERROR] {} {} - {}", method, url, ex.getMessage(), ex);

        BaseErrorCode code = ErrorCode._INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.onFailure(code, null));

    }

}
