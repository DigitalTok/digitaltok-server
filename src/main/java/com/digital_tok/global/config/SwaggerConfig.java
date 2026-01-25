package com.digital_tok.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. Security 스키마 설정 (JWT 토큰 방식)
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // 2. Components 설정 (헤더에 토큰을 어떻게 담을지 정의)
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP) // HTTP 방식
                .scheme("bearer") // Bearer 접두사를 사용
                .bearerFormat("JWT") // 포맷은 JWT
        );

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("환영합니다. DigitalTok API 명세서입니다!") // API 문서 제목
                .description("DigitalTok 서버 API 문서에요! 아마 잘 동작할거에요!!" +
                        "\n 회원가입 및 로그인 해서 AccessToken 발급받고, " +
                        "Authorize에 AccessToken입력하시면 모든 API 동작 확인 가능합니다! -> ->") // 설명
                .version("1.0.0"); // 버전
    }
}