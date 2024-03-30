package com.kmini.store.global.config.security.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.domain.user.dto.UserRequestDto.UserAPILoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;

public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper om = new ObjectMapper();

    public AjaxAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/login", POST.toString()), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        UserAPILoginDto apiLoginDto = om.readValue(request.getReader(), UserAPILoginDto.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = UsernamePasswordAuthenticationToken.unauthenticated(apiLoginDto.getEmail(), apiLoginDto.getPassword());

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
