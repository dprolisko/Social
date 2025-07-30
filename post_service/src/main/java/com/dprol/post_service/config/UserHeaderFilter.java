package com.dprol.post_service.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class UserHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String header = request.getHeader("UserId");
        if (header != null) {
            userContextConfig.setUserId(Long.parseLong(header));
        }
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }
        finally {
            userContextConfig.clearUserId();
        }
    }

    private final UserContextConfig userContextConfig;
}
