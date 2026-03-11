package com.schedula.schedula.config.Security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // Simple in-memory rate limiter per IP address
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    
    // 60 requests per minute for normal APIs
    private static final int CAPACITY = 60;
    private static final long REFILL_DURATION_MS = Duration.ofMinutes(1).toMillis();

    // 10 requests per minute for login API to prevent brute force
    private static final int LOGIN_CAPACITY = 10;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String ip = getClientIP(request);
        String path = request.getRequestURI();
        
        boolean isLoginRequest = path.contains("/auth/login");
        int capacity = isLoginRequest ? LOGIN_CAPACITY : CAPACITY;
        
        TokenBucket bucket = buckets.computeIfAbsent(ip + (isLoginRequest ? ":login" : ""), k -> new TokenBucket(capacity, REFILL_DURATION_MS));
        
        if (bucket.tryConsume()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"تم تجاوز الحد المسموح به من الطلبات. الرجاء المحاولة لاحقاً.\"}");
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
    
    private static class TokenBucket {
        private final int capacity;
        private final long refillDurationMs;
        private int tokens;
        private long lastRefillTime;

        public TokenBucket(int capacity, long refillDurationMs) {
            this.capacity = capacity;
            this.refillDurationMs = refillDurationMs;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTime;
            
            if (timePassed > refillDurationMs) {
                tokens = capacity;
                lastRefillTime = now;
            } else {
                // Calculate proportional tokens
                int newTokens = (int) ((timePassed * capacity) / refillDurationMs);
                if (newTokens > 0) {
                    tokens = Math.min(capacity, tokens + newTokens);
                    // Update last refill time based on how many tokens we actually added
                    lastRefillTime += (newTokens * refillDurationMs) / capacity;
                }
            }
        }
    }
}
