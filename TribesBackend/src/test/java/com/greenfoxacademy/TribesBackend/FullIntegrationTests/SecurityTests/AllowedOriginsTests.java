package com.greenfoxacademy.TribesBackend.FullIntegrationTests.SecurityTests;

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

import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ALLOWED_ORIGINS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class AllowedOriginsTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void before() {
        utilityMethods.clearDB();
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void testAllowedOrigins() throws Exception {
        user = utilityMethods.createEverything("some@email.com", "blah", 0, 0, List.of());
        token = utilityMethods.generateToken(user.getUsername(), ip, user.getId());
        for (String allowedOrigin : ALLOWED_ORIGINS) {
            mockMvc.perform(get("/kingdom")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Origin", allowedOrigin)
                    .header("Authorization", "Bearer " + token)
                    .with(request -> {
                        request.setRemoteAddr(ip);
                        return request;
                    })
                    .content("{ }"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    public void testNotAllowedOrigins() throws Exception {
        user = utilityMethods.createEverything("some@email.com", "blah", 0, 0, List.of());
        token = utilityMethods.generateToken(user.getUsername(), ip, user.getId());
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Origin", "somethingDefinitelyNotAllowedBlahBlah")
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ }"))
                .andExpect(status().is(403));
    }
}
