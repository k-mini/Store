package com.kmini.store.config.security.ajax.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.dto.response.UserDto;
import com.kmini.store.dto.response.UserDto.UserAPILoginSuccessDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper om = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserAPILoginSuccessDto userAPILoginSuccessDto =
                new UserAPILoginSuccessDto((AccountContext) authentication.getPrincipal());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

//        om.writeValue(response.getWriter(), authentication.getPrincipal());
        response.getWriter().println(om.writeValueAsString(userAPILoginSuccessDto));
    }
}
