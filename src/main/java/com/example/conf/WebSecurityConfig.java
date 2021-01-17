package com.example.conf;

import com.example.auth.domain.Role;
import com.example.auth.service.JwtAccessDeniedHandler;
import com.example.auth.service.JwtAuthTokenProvider;
import com.example.auth.service.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 시큐리티를 적용하고 설정하는 클래스
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// 보안상이거 그냥 꺼도되나?

                .exceptionHandling() // 에러처리
                .authenticationEntryPoint(authenticationErrorHandler) // 인증 오류시 처리
                .accessDeniedHandler(jwtAccessDeniedHandler) // 접근거부시 처리

                .and() // TODO 헤더 frameOptions, sameOrigin옵션 설정 확인
                .headers()
                .frameOptions()
                .sameOrigin()

                .and() // jwt토큰을 사용하므로 세션 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/login/**").permitAll()

                .antMatchers("/api/v1/coffees/**").hasAnyAuthority(Role.USER.getCode())
                .anyRequest().authenticated()

                .and()
                .apply(securityConfigurerAdapter()); // jwt시큐리티 설정 추가
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() // 인증없이 접근가능한 경로 설정
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers(
                        "/",
                        "/h2/console/**");
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(jwtAuthTokenProvider);
    }
}
