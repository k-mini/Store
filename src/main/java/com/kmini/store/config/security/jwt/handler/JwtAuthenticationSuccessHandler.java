package com.kmini.store.config.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.security.jwt.JwtTokenUtil;
import com.kmini.store.domain.User;
import com.kmini.store.dto.response.UserDto.UserAPILoginSuccessDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper om = new ObjectMapper();
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserAPILoginSuccessDto userAPILoginSuccessDto =
                new UserAPILoginSuccessDto((AccountContext) authentication.getPrincipal());
        User user = ((AccountContext) authentication.getPrincipal()).getUser();

        // 성공 시 토큰 발행
        Claims claims = Jwts.claims();
        // 이메일
        claims.put("email", user.getEmail());
        String token = jwtTokenUtil.createToken(claims);

        response.setHeader("token", token);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
//        response.setCharacterEncoding(UTF_8.toString());
//        om.writeValue(response.getWriter(), authentication.getPrincipal());
        response.getWriter().println(om.writeValueAsString(userAPILoginSuccessDto));
    }
}
