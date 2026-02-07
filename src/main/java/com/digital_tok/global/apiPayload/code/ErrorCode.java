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
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증되어있지 않습니다."),
    
    // Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "MEMBER409_1", "이미 가입된 사용자입니다."),
    
    // Device 관련 에러
    DEVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "DEVICE404", "요청한 기기를 찾을 수 없습니다."),
    DEVICE_ALREADY_CONNECTED(HttpStatus.BAD_REQUEST, "DEVICE400", "기기가 이미 연결되어 있습니다."),
    DEVICE_ALREADY_DISCONNECTED(HttpStatus.BAD_REQUEST, "DEVICE401", "기기가 연결 해제되어 있습니다."),
    // Image 관련 에러
    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE500_1", "이미지 업로드에 실패했습니다."), // 세미콜론(;) 주의
    IMAGE_TO_BINARY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE500_2", "이미지를 바이너리 데이터로 변환시키기에 실패했습니다."),

    // Template 관련 에러
    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "TEMPLATE404_1", "해당 템플릿을 찾을 수 없습니다."),
    ;
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
