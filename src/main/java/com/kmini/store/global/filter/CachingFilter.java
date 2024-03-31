package com.kmini.store.global.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper cachingRequestWrapper
                = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper
                = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
    }
}
