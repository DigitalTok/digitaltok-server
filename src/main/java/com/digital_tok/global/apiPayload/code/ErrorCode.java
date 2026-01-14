package com.digital_tok.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode{

    // 공통 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400_1", "잘못된 입력 값입니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405_1", "허용되지 않은 메서드입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404_1", "요청한 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의하세요."),

    
    // Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEMBER409_1", "이미 가입된 사용자입니다.")
    
    // Device 관련 에러
    
    // Image 관련 에러
    
    // subway 관련 에러
    ;
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
