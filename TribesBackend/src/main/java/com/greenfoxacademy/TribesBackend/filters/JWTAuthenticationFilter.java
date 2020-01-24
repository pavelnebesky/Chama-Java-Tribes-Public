package com.greenfoxacademy.TribesBackend.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.greenfoxacademy.TribesBackend.configs.CachedBodyHttpServletRequest;
import com.greenfoxacademy.TribesBackend.services.TimeService;
import com.greenfoxacademy.TribesBackend.services.UtilityService;
import com.greenfoxacademy.TribesBackend.models.BlacklistedToken;
import com.greenfoxacademy.TribesBackend.repositories.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TimeService timeService;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    private boolean isBlackListed(String token) {
        List<BlacklistedToken> blacklistedTokens = (List<BlacklistedToken>) blacklistedTokenRepository.findAll();
        for (BlacklistedToken blacklistedToken : blacklistedTokens) {
            if (blacklistedToken.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    private void updateBlackList() {
        List<BlacklistedToken> blacklistedTokens = (List<BlacklistedToken>) blacklistedTokenRepository.findAll();
        for (BlacklistedToken blacklistedToken : blacklistedTokens) {
            if (JWT.decode(blacklistedToken.getToken()).getExpiresAt().before(new Date())) {
                blacklistedTokenRepository.deleteById(blacklistedToken.getId());
            }
        }
    }

    private boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) throws JWTVerificationException {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            DecodedJWT decodedjwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String expectedIp = decodedjwt.getHeaderClaim(IP_CLAIM).asString();
            String actualIp = request.getRemoteAddr();
            if (actualIp.equals(expectedIp) && !isBlackListed(decodedjwt.getToken())) {
                updateBlackList();
                timeService.updateAllUserData(utilityService.getIdFromToken(request));
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(req);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(res);
        Date requestDate = new Date();
        String header = req.getHeader(HEADER_STRING);
        try {
            if (List.of(PUBLIC_ENDPOINTS).stream().anyMatch(e -> req.getRequestURI().contains(e))
                    || (header != null && header.startsWith(TOKEN_PREFIX) && isAuthorized(req, res))) {
                chain.doFilter(cachedBodyHttpServletRequest, contentCachingResponseWrapper);
            } else {
                res.sendError(401, "You are unauthorized!");
            }
        } catch (JWTVerificationException e) {
            res.sendError(401, "You are unauthorized!");
        }
        Date responseDate = new Date();
        utilityService.logRequestResponse(cachedBodyHttpServletRequest, contentCachingResponseWrapper, requestDate, responseDate);
        contentCachingResponseWrapper.copyBodyToResponse();
    }
}
