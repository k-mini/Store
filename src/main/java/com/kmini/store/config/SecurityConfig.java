package com.kmini.store.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Profile({"default","local"})
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨 ");
        // CSRF 끄기
        http.csrf().disable();
        // 인가 정책
        http.authorizeRequests()
                .antMatchers("/auth/signin", "/auth/signup").anonymous()
                .antMatchers("/api/**", "/boards/**", "/board/**", "/auth/my-page").authenticated()
                .anyRequest().permitAll();

        // 로그인 방식
        http
                .formLogin()
                .usernameParameter("email")
                .loginPage("/auth/signin")
                .loginProcessingUrl("/auth/signin")
                .defaultSuccessUrl("/")
                .failureUrl("/auth/signin");

        // 로그아웃 처리
        http.logout()
                .logoutSuccessUrl("/");

        // 인가예외 처리
        http.exceptionHandling()
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/");
                        });

        // 세션 정책
        http.sessionManagement()
                        .maximumSessions(1);

        // h2 테스트 환경
        http.headers().frameOptions().disable();

        return http.build();
    }
}
