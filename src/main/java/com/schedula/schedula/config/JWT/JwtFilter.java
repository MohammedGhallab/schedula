package com.schedula.schedula.config.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
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
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = null;

            // 1. استخراج التوكن من الكوكيز
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token != null && tokenBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // 2. التحقق من التوكن وصحة المستخدم
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.extractUserName(token);

                if (username != null && jwtService.validateToken(token, username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 3. إعلام Spring Security بأن المستخدم موثق
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("تعذر ضبط توثيق المستخدم: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
    /*
     * @Override
     * protected void doFilterInternal(HttpServletRequest request,
     * HttpServletResponse response, FilterChain filterChain)
     * throws ServletException, IOException {
     * String authHeader = request.getHeader("Authorization");
     * String token = null;
     * // TODO أقوم بتطبيق سحب التوكن من المتغير لأنه يرجع string
     * Cookie[] cookies = request.getCookies();
     * String sessionID = null;
     * String userName = null;
     * if (cookies != null) {
     * for (Cookie cookie : cookies) {
     * if (cookie.getName().equals("SESSION_ID")) {
     * sessionID = cookie.getValue();
     * }
     * if (sessionID != null) {
     * if (cookie.getName().equals("SESSION_" + sessionID)) {
     * userName = cookie.getValue();
     * LoginResponse sessionData = (LoginResponse)
     * redisService.retrieveFromHash(sessionID, userName);
     * token = sessionData.getToken();
     * }
     * }
     * }
     * }
     * if (authHeader != null && authHeader.startsWith("Bearer ")) {
     * token = authHeader.substring(7);
     * userName = jwtService.extractUserName(token);
     * }
     * if (token != null && tokenBlacklistService.isBlacklisted(token)) {
     * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
     * return;
     * }
     * if (userName != null &&
     * SecurityContextHolder.getContext().getAuthentication() == null) {
     * // UserDetails userDetails =
     * //
     * context.getBean(UserDetailsDataService.class).loadUserByUsername(username);
     * if (jwtService.validateToken(token, userName)) {
     * UserDetails userDetails =
     * context.getBean(UserDetailsDataService.class).loadUserByUsername(userName);
     * UsernamePasswordAuthenticationToken authToken = new
     * UsernamePasswordAuthenticationToken(userDetails,
     * null, userDetails.getAuthorities());
     * authToken.setDetails(new WebAuthenticationDetailsSource()
     * .buildDetails(request));
     * SecurityContextHolder.getContext().setAuthentication(authToken);
     * }
     * }
     * 
     * filterChain.doFilter(request, response);
     * }
     */
}