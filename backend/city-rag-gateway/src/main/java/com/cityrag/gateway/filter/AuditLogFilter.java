package com.cityrag.gateway.filter;

import com.cityrag.common.constant.DataLevel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuditLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String userId = request.getHeader("X-User-Id");

            // Log access for L3 sensitive data paths
            if (uri.contains("/api/v1/buildings") || uri.contains("/api/v1/population")) {
                log.info("[AUDIT] method={}, uri={}, userId={}, duration={}ms, status={}",
                        method, uri, userId != null ? userId : "anonymous", duration, response.getStatus());
            }
        }
    }
}
