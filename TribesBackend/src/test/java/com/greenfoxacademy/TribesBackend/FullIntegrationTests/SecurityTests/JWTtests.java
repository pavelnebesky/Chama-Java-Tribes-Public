package com.greenfoxacademy.TribesBackend.FullIntegrationTests.SecurityTests;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class JWTtests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("something@sth.com", "kingdomName", 10000, 10000, List.of(BuildingType.barracks, BuildingType.townhall));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void noTokenTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void incorrectTokenTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token + "1")
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void noPrefixTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void expiredTokenTest() throws Exception {
        Map<String, Object> headerMap = Map.of(IP_CLAIM, ip);
        token = JWT.create()
                .withHeader(headerMap)
                .withClaim(ID_CLAIM, String.valueOf(user.getId()))
                .withClaim(USERNAME_CLAIM, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() - 1000))
                .sign(HMAC512(SECRET.getBytes()));
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void incorrectIpTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip + "1");
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void noIpTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content("{}"))
                .andExpect(status().is(401));
    }

    @Test
    public void everythingFineTest() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
