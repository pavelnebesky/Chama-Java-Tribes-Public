package com.greenfoxacademy.TribesBackend.services;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;

@Getter
@Service
public class UtilityService {

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

    public Long getIdFromToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        return Long.parseLong(JWT.decode(token).getClaim(ID_CLAIM).asString());
    }
}