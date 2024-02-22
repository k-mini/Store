package com.kmini.store.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 끄기
        http.csrf().disable();
        // 인가 정책
        http.authorizeRequests()
//                .antMatchers("/docs/**").hasRole("MANAGER")
//                .antMatchers("/auth/signin", "/auth/signup","/api/user").anonymous()
//                .antMatchers("/api/**", "/boards/**", "/board/**", "/auth/my-page","/user/**").authenticated()
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
//        http.headers().frameOptions().sameOrigin();
//        http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));

        return http.build();
    }

//    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .antMatchers("/assets/**");
    }
}
