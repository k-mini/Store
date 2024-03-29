package com.kmini.store.config.security.jwt;

import com.kmini.store.config.security.jwt.handler.JwtAccessDeniedHandler;
import com.kmini.store.config.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.kmini.store.config.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.kmini.store.config.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.kmini.store.config.security.oauth.OauthAuthenticationSuccessHandler;
import com.kmini.store.config.security.oauth.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenUtil jwtTokenUtil;
    private final OauthService oauthService;

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
                .antMatchers("/api/user/authentication").authenticated()
//                .antMatchers("/api/user/**").authenticated()
                .anyRequest().permitAll();

        // 로그인 방식
        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 성공 처리

        // 로그아웃 처리
        http.logout()
                .logoutSuccessUrl("/");

        // 매니저 등록
//        http.authenticationManager(ajaxAuthenticationManager());

        // 인가예외 처리
        http.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler())
                .authenticationEntryPoint(jwtAuthenticationEntryPoint());

        // 세션 정책
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // oauth2
        http.oauth2Login()
//                .defaultSuccessUrl("http://localhost:9090", true)
                .successHandler(oauthAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oauthService);
        return http.build();
    }

    @Bean
    public AuthenticationManager jwtAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtAuthenticationManager(), jwtTokenUtil);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationManager(jwtAuthenticationManager());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler());
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }
    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(jwtTokenUtil);
    }
    @Bean
    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }
    @Bean
    public OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler() {
        return new OauthAuthenticationSuccessHandler(jwtTokenUtil);
    }
}
