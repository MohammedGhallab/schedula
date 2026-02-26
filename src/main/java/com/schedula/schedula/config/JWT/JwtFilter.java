package com.schedula.schedula.config.JWT;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.schedula.schedula.user.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromCookies(request);

            // 1. التحقق من وجود التوكن ومن القائمة السوداء
            if (token != null) {
                if (tokenBlacklistService.isBlacklisted(token)) {
                    log.warn("Token is blacklisted");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return; // توقف هنا لأن التوكن مرفوض تماماً
                }

                // 2. التحقق وبناء الـ Security Context إذا لم يكن موجوداً مسبقاً
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    if (jwtService.isTokenValid(token)) {
                        // استخراج البيانات من الـ Claims
                        UUID userId = jwtService.extractUserId(token);
                        String username = jwtService.extractUserName(token);
                        String role = jwtService.extractRole(token);
                        String name = jwtService.extractFullName(token);
                        Boolean isActive = jwtService.extractIsActive(token);

                        if (username != null && role != null) {
                            // التأكد من تنسيق الدور لـ Spring Security
                            String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                            
                            List<SimpleGrantedAuthority> authorities = Collections
                                    .singletonList(new SimpleGrantedAuthority(formattedRole));

                            // بناء كائن الـ UserDetails (كلمة المرور فارغة لأننا Stateless)
                            CustomUserDetails userDetails = new CustomUserDetails(
                                    userId, username, "", isActive != null && isActive, role, name, username);

                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, authorities);

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            
                            // 3. تثبيت التوثيق في السياق
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            log.debug("Authenticated user: {}", username);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context: {}", e.getMessage());
            // اختيارياً: يمكنك مسح الـ Context هنا
            SecurityContextHolder.clearContext();
        }

        // دائماً استمر في السلسلة للطلبات الأخرى
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}