package com.digital_tok.global.security;

import com.digital_tok.user.domain.User;
import com.digital_tok.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.digital_tok.user.domain.UserStatus;

import java.io.IOException;

// (패키지, 임포트 동일)

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우만 Authentication 객체 생성
                Long userId = jwtTokenProvider.getUserId(token);

                User user = userRepository.findById(userId).orElse(null);



                // 유저가 존재하고, 상태가 ACTIVE(활동 중)인 경우에만 인증 처리
                if (user != null && user.getStatus() == UserStatus.ACTIVE) {
                    PrincipalDetails principalDetails = new PrincipalDetails(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else if (user != null && user.getStatus() != UserStatus.ACTIVE) {
                    log.warn("탈퇴하거나 정지된 계정의 접근 시도입니다. userId: {}", userId);
                    // 여기서 바로 에러 응답을 보내거나, SecurityContext를 비워두어 뒤쪽 필터에서 401/403이 뜸
                }
            }
        } catch (Exception e) {
            // 🚨 중요: 토큰 검증 중 에러가 나도(만료, 위조 등) SecurityContext에 저장만 안 할 뿐,
            // 필터 체인은 계속 진행시켜야 합니다. 그래야 permitAll 경로(Swagger 등)로 들어갈 수 있습니다.
            log.error("토큰 검증 실패 (로그인 정보 없이 요청 처리): {}", e.getMessage());
        }

        // 3. 다음 필터로 진행 (이게 없으면 하얀 화면만 뜸)
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}