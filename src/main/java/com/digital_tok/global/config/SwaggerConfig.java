package com.digital_tok.global.config;

import com.digital_tok.global.apiPayload.code.ApiErrorCodes;
import com.digital_tok.global.apiPayload.code.ErrorCode;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .description("DiRing 서버 API 문서입니다!" +
                        "\n 회원가입 및 로그인 해서 AccessToken 발급받고, " +
                        "Authorize에 AccessToken입력하시면 모든 API 동작 확인 가능합니다!") // 설명
                .version("1.0.0"); // 버전
    }

    // 예외처리 문서화 관련 커스텀 어노테이션
    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);

            // 어노테이션이 없으면 그냥 넘어감
            if (apiErrorCodes == null) {
                return operation;
            }

            // 1. 에러 코드들을 HTTP 상태 코드별로 그룹화 (예: 400끼리, 404끼리)
            Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(apiErrorCodes.value())
                    .map(this::generateExampleHolder)
                    .collect(Collectors.groupingBy(ExampleHolder::getStatusCode));

            // 2. ApiResponses에 등록
            addExamplesToResponses(operation.getResponses(), statusWithExampleHolders);

            return operation;
        };
    }

    // ErrorCode를 받아서 내부용 ExampleHolder 객체로 변환
    private ExampleHolder generateExampleHolder(ErrorCode errorCode) {
        return ExampleHolder.builder()
                .statusCode(errorCode.getStatus().value())
                .holderName(errorCode.name()) // 예: MEMBER_NOT_FOUND
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    // Swagger Response에 예시 추가하는 로직
    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach((status, examples) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();

            // 하나의 상태 코드에 여러 예시가 있을 경우 (예: 같은 400인데 비번틀림, 파라미터오류 등)
            examples.forEach(example -> {
                mediaType.addExamples(example.getHolderName(), new Example()
                        .value(generateErrorResponse(example.getCode(), example.getMessage())) // JSON 생성
                        .description(example.getMessage())); // 설명 추가
            });

            content.addMediaType("application/json", mediaType);

            // 기존에 200 응답만 있고 에러 응답이 없으면 새로 추가
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setContent(content);
            apiResponse.setDescription("에러 응답 (상세 예시는 Example Value 확인)");

            responses.addApiResponse(String.valueOf(status), apiResponse);
        });
    }

    // 실제 JSON 형태 만들기
    private Map<String, Object> generateErrorResponse(String code, String message) {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("isSuccess", false);
        map.put("code", code);
        map.put("message", message);
        map.put("result", null); // 이제 null도 안전하게 들어감
        return map;
    }

    // 내부에서 사용할 DTO
    @lombok.Builder
    @lombok.Getter
    private static class ExampleHolder {
        private int statusCode;
        private String holderName;
        private String code;
        private String message;
    }
}