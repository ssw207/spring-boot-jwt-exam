package com.example.conf;

import com.example.auth.dto.JwtAuthToken;
import com.example.auth.service.JwtAuthTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JWTFilter extends GenericFilterBean { // TODO GenericFilterBean 가 하는 역할 확인

    private static final String AUTHORIZATION_HEADER = "x-auth-token"; // 헤더에 세팅할 jwt 토크명
    private JwtAuthTokenProvider jwtAuthTokenService;

    JWTFilter(JwtAuthTokenProvider jwtAuthTokenService) {
        this.jwtAuthTokenService = jwtAuthTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Optional<String> token = resolveToken(httpServletRequest);

        //토큰이 있으면
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenService.convertAuthToken(token.get());

            //토큰이 유효하면
            if(jwtAuthToken.validate()) {
                Authentication authentication = jwtAuthTokenService.getAuthentication(jwtAuthToken); //토큰으로부터 인증객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication); // 시큐리티 인증객체 설정
            }
        }
    }

    /**
     * 헤더에서 jwt인증토큰이 있으면 꺼낸다
     * @param httpServletRequest
     * @return
     */
    private Optional<String> resolveToken(HttpServletRequest httpServletRequest) {
        //헤더에서 jwt인증토큰을 꺼낸다
        String authToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }
}
