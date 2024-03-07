package com.kmini.store.config.security.oauth;

import com.kmini.store.config.security.jwt.JwtTokenUtil;
import com.kmini.store.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        User user = ((CustomOAuth2User) authentication.getPrincipal())
                .getAccountContext()
                .getUser();

        String email = user.getEmail();
        // 성공 시 토큰 발행
        Claims claims = Jwts.claims();
        // 이메일
        claims.put("email", email);
        String token = jwtTokenUtil.createToken(claims);

        String redirectUrl = String.format("http://localhost:9090?token=%s", token);
        response.sendRedirect(redirectUrl);
    }
}
