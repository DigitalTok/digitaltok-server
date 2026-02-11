package com.digital_tok.global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck() {
        // 나중에 현재 실행 중인 프로필(dev/prod) 등을 반환하게 확장할 수도 있습니다.
        return "잘 작동중입니다~~";
    }
}