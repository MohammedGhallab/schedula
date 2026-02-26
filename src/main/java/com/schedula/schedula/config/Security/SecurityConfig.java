package com.schedula.schedula.config.Security;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.schedula.schedula.config.JWT.JwtFilter;
import com.schedula.schedula.user.services.UserDetailsDataService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsDataService userDetailsDataService;
    private final JwtFilter jwtFilter;

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // تم تعطيله لأننا نستخدم JWT (Stateless)
                .exceptionHandling(handle -> handle
                        // 1. معالجة حالة: غير مسجل دخول (أو الـ Token غير صالح)
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(
                                    "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"عذراً، يجب تسجيل الدخول للوصول إلى هذه الخدمة\"}");
                        })
                        // 2. معالجة حالة: مسجل دخول ولكن يحاول الوصول لمورد غير مصرح له به
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(
                                    "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"ليس لديك الصلاحيات الكافية للوصول إلى هذا القسم\"}");
                        }))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll() // للسماح بالـ Swagger
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsDataService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        // configuration.setAllowedOrigins(Arrays.asList(env.getProperty("app.frontend.url")));
        // TODO حذف هذا السطر وتفعيل الي قبل في الإنتاج، استبدل "*" برابط الـ Frontend
        // الحقيقي
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
/*
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * 
 * @EnableMethodSecurity
 * 
 * @RequiredArgsConstructor
 * public class SecurityConfig {
 * 
 * private final UserDetailsDataService userDetailsDataService;
 * // private final CustomAccessDeniedHandler accessDeniedHandler;
 * private final JwtFilter jwtFilter;
 * private final Environment env;
 * 
 * @Bean
 * public DefaultSecurityFilterChain securityFilterChain(
 * HttpSecurity http, RateLimitingFilter rateLimitingFilter) {
 * 
 * // try {
 * return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
 * .csrf(csrf -> csrf.disable()).exceptionHandling(handle -> handle
 * .authenticationEntryPoint(
 * ((request, response, authException) -> {
 * System.out.println("the error its : ");
 * // response.sendRedirect(env.getProperty("app.frontend.url") + "/401");
 * // response.sendError(
 * // HttpServletResponse.SC_UNAUTHORIZED,
 * // authException.getMessage());
 * })))
 * .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
 * .sessionManagement(session -> session
 * .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/auth/**")
 * .permitAll()
 * .anyRequest().authenticated())
 * // .addFilterBefore(
 * // rateLimitingFilter,
 * // org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter.class)
 * .build();
 * // } catch (Exception e) {
 * // System.out.println("the error in security config: " + e.getMessage());
 * // throw new RuntimeException("Failed to build security filter chain", e);
 * // }
 * }
 * 
 * @Bean
 * public CsrfTokenRepository csrfTokenRepository() {
 * CookieCsrfTokenRepository csrfTokenRepository =
 * CookieCsrfTokenRepository.withHttpOnlyFalse();
 * csrfTokenRepository.setCookieCustomizer(cookie -> cookie.httpOnly(true));
 * return csrfTokenRepository;
 * }
 * 
 * // @Bean
 * public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
 * FilterRegistrationBean<RateLimitFilter> registration = new
 * FilterRegistrationBean<>();
 * registration.setFilter(new RateLimitFilter());
 * return registration;
 * }
 * 
 * // @Bean
 * // public AuthenticationProvider authenticationProvider() {
 * // DaoAuthenticationProvider provider = new
 * // DaoAuthenticationProvider(userDetailsDataService);
 * // // ضبط مشفر كلمة المرور
 * // provider.setPasswordEncoder(passwordEncoder());
 * 
 * // return provider;
 * // }
 * 
 * @Bean
 * public PasswordEncoder passwordEncoder() {
 * // استخدم BCrypt الافتراضي لضمان مطابقة الـ Hash الموجود في قاعدة بياناتك
 * return new BCryptPasswordEncoder();
 * }
 * 
 * @Bean
 * public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration config) throws Exception {
 * return config.getAuthenticationManager();
 * 
 * }
 * 
 * @Bean
 * public AuthenticationProvider authenticationProvider() {
 * DaoAuthenticationProvider provider = new
 * DaoAuthenticationProvider(userDetailsDataService);
 * // تمرير مشفر كلمة المرور
 * provider.setPasswordEncoder(passwordEncoder());
 * return provider;
 * }
 * 
 * @Bean
 * CorsConfigurationSource corsConfigurationSource() {
 * CorsConfiguration configuration = new CorsConfiguration();
 * configuration.setAllowCredentials(true);
 * 
 * configuration.setAllowedOriginPatterns(Arrays.asList("*"));
 * configuration.setAllowedHeaders(
 * Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
 * "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
 * "Access-Control-Request-Method", "Access-Control-Request-Headers",
 * "grpc-web-content-type", "x-grpc-web", "x-user-agent"));
 * configuration.setExposedHeaders(Arrays.asList("Origin", "Content-Type",
 * "Accept", "Authorization",
 * "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
 * "Access-Control-Allow-Credentials",
 * "grpc-status", "grpc-message", "x-grpc-web", "x-user-agent"));
 * configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
 * "OPTIONS"));
 * UrlBasedCorsConfigurationSource source = new
 * UrlBasedCorsConfigurationSource();
 * source.registerCorsConfiguration("/**", configuration);
 * return source;
 * }
 * 
 * }
 */
