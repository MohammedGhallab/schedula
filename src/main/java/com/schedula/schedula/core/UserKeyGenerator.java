package com.schedula.schedula.core;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Component("userKeyGenerator")
public class UserKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        String userId = "1";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().isEmpty()) {
            userId = authentication.getName();
        }

        return "user:" + userId;
    }
}