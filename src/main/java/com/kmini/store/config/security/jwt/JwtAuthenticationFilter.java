package com.kmini.store.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.UserDto.UserAPILoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.POST;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper om = new ObjectMapper();
    private final JwtTokenUtil jwtTokenUtil;
    private final int BEARER_IDX = 7;

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(new AntPathRequestMatcher("/api/login", POST.toString()), authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!requiresAuthentication(request, response)) {
            checkJwtToken(request, response);
            chain.doFilter(request, response);
            return;
        }
        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            successfulAuthentication(request, response, chain, authenticationResult);
        }
        catch (InternalAuthenticationServiceException failed) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);
        }
        catch (AuthenticationException ex) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, ex);
        }
    }

    private void checkJwtToken(HttpServletRequest request, HttpServletResponse response) {

        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
            return;
        }
        String jwt = bearerToken.substring(BEARER_IDX);

        Authentication authentication = jwtTokenUtil.getAuthentication(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        UserAPILoginDto apiLoginDto = om.readValue(request.getReader(), UserAPILoginDto.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = UsernamePasswordAuthenticationToken.unauthenticated(apiLoginDto.getEmail(), apiLoginDto.getPassword());

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
