package com.digital_tok;

import com.digital_tok.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        // 프로젝트의 공통 응답 형식인 ApiResponse를 사용하여 성공 메시지를 반환합니다.
        return ApiResponse.onSuccessResultOnly("잘 작동중입니다~~");
    }
}