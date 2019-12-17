package com.greenfoxacademy.TribesBackend.constants;

public class SecurityConstants {
    public static final String SECRET = "nyUGt6VrrC1qLLg9b9xN";
    public static final long EXPIRATION_TIME = 3_600_000; // 1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/register";
    public static final String SIGN_IN_URL = "/login";
    public static final String VERIFY_URL = "/verify";
    public static final String ID_CLAIM = "id";
    public static final String IP_CLAIM = "ip";
}
