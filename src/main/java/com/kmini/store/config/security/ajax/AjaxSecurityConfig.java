package com.kmini.store.config.security.ajax;

import com.kmini.store.config.security.ajax.handler.AjaxAccessDeniedHandler;
import com.kmini.store.config.security.ajax.handler.AjaxAuthenticationEntryPoint;
import com.kmini.store.config.security.ajax.handler.AjaxAuthenticationFailureHandler;
import com.kmini.store.config.security.ajax.handler.AjaxAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
//@EnableWebSecurity
public class AjaxSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 끄기
        http.csrf().disable();
        // 인가 정책
        http.authorizeRequests()
                .anyRequest().permitAll();

        // 로그인 방식
        http
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 성공 처리

        // 로그아웃 처리
        http.logout()
                .logoutSuccessUrl("/");

        // 매니저 등록
//        http.authenticationManager(ajaxAuthenticationManager());

        // 인가예외 처리
        http.exceptionHandling()
                .accessDeniedHandler(ajaxAccessDeniedHandler())
                .authenticationEntryPoint(ajaxAuthenticationEntryPoint());

        // 세션 정책
        http.sessionManagement()
                .maximumSessions(1);
        return http.build();
    }

    @Bean
    public AuthenticationManager ajaxAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        AjaxAuthenticationFilter ajaxAuthenticationFilter = new AjaxAuthenticationFilter(ajaxAuthenticationManager());
        ajaxAuthenticationFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxAuthenticationFilter.setAuthenticationManager(ajaxAuthenticationManager());
        ajaxAuthenticationFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxAuthenticationFilter;
    }

    @Bean
    public AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
        return new AjaxAuthenticationEntryPoint();
    }
    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new AjaxAccessDeniedHandler();
    }
    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }
    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }
}
