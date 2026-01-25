package com.digital_tok.global.security;

import com.digital_tok.domain.User;
import com.digital_tok.repository.UserRepository;
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

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository; // DB 조회를 위해 필요

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰에서 userId 추출
            Long userId = jwtTokenProvider.getUserId(token);

            // 4. DB에서 유저 정보 조회 (실존하는지, 차단되진 않았는지 확인)
            // 성능 이슈가 걱정된다면 DB 조회 없이 Claims만으로 Authentication을 만들 수도 있음.
            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user != null) {
                // 5. UserDetails 생성
                PrincipalDetails principalDetails = new PrincipalDetails(user);

                // 6. Authentication 객체 생성 (비밀번호는 null로 설정)
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails,
                        null,
                        principalDetails.getAuthorities()
                );

                // 7. SecurityContext에 저장 (이제 전역에서 유저 정보를 쓸 수 있음!)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 헤더에서 Bearer 토큰 꺼내기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}