package com.greenfoxacademy.TribesBackend.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;
import static com.greenfoxacademy.TribesBackend.services.AuthenticationService.publicEndpoints;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    private boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            try {
                DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""));
                String tokenIp = decodedjwt.getHeaderClaim(IP_CLAIM).asString();
                String actualIp = request.getRemoteAddr();
                return actualIp.equals(tokenIp);
            } catch (SecurityException e) {
                response.setStatus(401);
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header = req.getHeader(HEADER_STRING);
        if (publicEndpoints.contains(req.getRequestURI())
                || (header != null && header.startsWith(TOKEN_PREFIX) && isAuthorized(req, res))) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(401);
        }
    }
}
