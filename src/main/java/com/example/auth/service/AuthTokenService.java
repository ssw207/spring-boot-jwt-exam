package com.example.auth.service;

import org.springframework.security.core.Authentication;

import java.util.Date;

public interface AuthTokenService<T> {
    T createAuthToken(String id, String role, Date expiredDate);
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
}
