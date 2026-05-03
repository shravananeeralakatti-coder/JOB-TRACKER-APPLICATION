package com.shravana.jobtracker.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // ✅ Allow PUBLIC endpoints (VERY IMPORTANT)
        if (path.startsWith("/auth") ||                 // login/register APIs
            path.equals("/") ||                         // root
            path.equals("/login.html") ||               // login page
            path.equals("/index.html") ||               // main page
            path.endsWith(".html") ||                   // any HTML
            path.endsWith(".js") ||                     // JS files
            path.endsWith(".css") ||                    // CSS files
            path.endsWith(".png") ||                    // images
            path.endsWith(".jpg") ||
            path.endsWith(".jpeg")) {

            chain.doFilter(request, response);
            return;
        }

        // 🔐 Get Authorization header
        String header = req.getHeader("Authorization");

        // ❌ No token
        if (header == null || !header.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Missing Token");
            return;
        }

        // 🔐 Extract token
        String token = header.substring(7);

        // ❌ Invalid token
        if (!jwtUtil.validateToken(token)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        // ✅ Valid → allow request
        chain.doFilter(request, response);
    }
}