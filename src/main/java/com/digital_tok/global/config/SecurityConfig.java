package com.digital_tok.global.config;

import com.digital_tok.global.security.JwtAuthenticationFilter;
import com.digital_tok.global.security.JwtTokenProvider;
import com.digital_tok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 1. 비밀번호 암호화 Bean 등록 (BCrypt 방식)
    // 회원가입 시 비밀번호를 암호화하거나, 로그인 시 비밀번호를 확인할 때 사용합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Security Filter Chain 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                // ORS 설정 연결
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))

                // CSRF 비활성화 (REST API 서버는 세션을 사용하지 않으므로 보통 비활성화)
                .csrf(AbstractHttpConfigurer::disable)

                // Form Login 비활성화 (JWT 방식을 쓸 것이므로)
                .formLogin(AbstractHttpConfigurer::disable)

                // HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션 관리 설정: STATELESS (서버에 세션을 저장하지 않음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // ★ 1. Preflight(OPTIONS) 요청은 인증 없이 무조건 허용 (CORS 해결 핵심)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/health", "/actuator/prometheus").permitAll()

                        // 인증 없이 접근 가능한 경로 (로그인, 회원가입, 스웨거 등)
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                        // ADMIN 전용 - 템플릿 이미지 생성은 관리자만 가능
                        .requestMatchers("/api/v1/templates/subway/generate").hasRole("ADMIN")

                        // 그 외 모든 요청은 인증(로그인) 필요
                        .anyRequest().authenticated()
                )

        // 필터 등록 (UsernamePasswordAuthenticationFilter 앞에 실행)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CROS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. 허용할 프론트엔드 도메인 (운영 서버 + 로컬 개발용)
        configuration.setAllowedOrigins(List.of("https://diring.site", "https://www.diring.site", "http://localhost:8080"));

        // 2. 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 3. 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 4. 자격 증명(쿠키/토큰) 허용
        configuration.setAllowCredentials(true);

        // 5. 클라이언트가 읽을 수 있는 헤더 (JWT 토큰 반환 시 필요할 수 있음)
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}