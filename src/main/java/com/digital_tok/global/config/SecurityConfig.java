package com.digital_tok.global.config;

import com.digital_tok.global.security.JwtAuthenticationFilter;
import com.digital_tok.global.security.JwtTokenProvider;
import com.digital_tok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                        // 인증 없이 접근 가능한 경로 (로그인, 회원가입, 스웨거 등)
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/health").permitAll()

                        // 그 외 모든 요청은 인증(로그인) 필요
                        .anyRequest().authenticated()
                )

        // 필터 등록 (UsernamePasswordAuthenticationFilter 앞에 실행)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}