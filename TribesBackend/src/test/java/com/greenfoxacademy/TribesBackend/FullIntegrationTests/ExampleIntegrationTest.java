package com.greenfoxacademy.TribesBackend.FullIntegrationTests;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;
import static org.hamcrest.core.Is.is;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.exceptions.EmailAlreadyTakenException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class ExampleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UtilityMethods utilityMethods;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void setup() {
        user = utilityMethods.createEverything("something@sth.com", "kingdomName", 10000, 10000, List.of(BuildingType.barracks, BuildingType.townhall));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
    }

    @AfterEach
    public void deSetup() {
        userRepository.deleteAll();
        kingdomRepository.deleteAll();
        buildingRepository.deleteAll();
        resourceRepository.deleteAll();
    }

    @Test
    public void justTesting() throws Exception {
        mockMvc.perform(get("/kingdom")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is("kingdomName")))
                .andExpect(jsonPath("userId", is(user.getId().intValue())));
    }
}

