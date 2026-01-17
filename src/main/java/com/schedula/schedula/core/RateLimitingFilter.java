package com.schedula.schedula.core;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements Filter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    // private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    public RateLimitingFilter() {
        // جدولة مهمة لتفريغ الخريطة كل 60 ثانية
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(requestCounts::clear, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr();
        
        // if (httpRequest.getRequestURI().equals("/favicon.ico")) {
        //     // chain.doFilter(request, response);
        //     return;
        // }
        // الحصول على العداد أو إنشاء واحد جديد
        AtomicInteger count = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
        int currentCount = count.incrementAndGet();
        System.out.println("the URL : " + httpRequest.getRequestURL() + " Count : " + count);
        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.setContentType("text/plain; charset=UTF-8");
            httpResponse.getWriter().write("لقد تجاوزت الحد المسموح. انتظر حتى بداية الدقيقة القادمة. " + currentCount
                    + " : " + MAX_REQUESTS_PER_MINUTE);
            return;
        }

        chain.doFilter(request, response);
    }
}