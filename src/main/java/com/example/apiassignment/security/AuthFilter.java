package com.example.apiassignment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String DUMMY_TOKEN = "DUMMY_AUTH_TOKEN_123";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        String path = request.getRequestURI();
        return path.contains("api/auth/login")
                || path.contains("/swagger")
                || path.contains("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow login without auth
        if (path.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Read cookie
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        boolean authenticated = false;

        for (Cookie cookie : cookies) {
            if ("AUTH_TOKEN".equals(cookie.getName())
                    && DUMMY_TOKEN.equals(cookie.getValue())) {
                authenticated = true;
                break;
            }
        }

        if (!authenticated) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
