package com.greenfoxacademy.TribesBackend.services;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;

@Getter
@Service
public class AuthenticationService {

    public static final List<String> publicEndpoints = List.of("/login", "/register");

    @Autowired
    private UserRepository userRepository;

    public String generateJWT(String ip, Long id) {
        Map<String, Object> headerMap = Map.of(IP_CLAIM, ip);
        return JWT.create()
                .withHeader(headerMap)
                .withClaim(ID_CLAIM, String.valueOf(id))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }
}
