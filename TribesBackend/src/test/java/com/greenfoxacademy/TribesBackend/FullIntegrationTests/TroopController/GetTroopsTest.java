package com.greenfoxacademy.TribesBackend.FullIntegrationTests.TroopController;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")

public class GetTroopsTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private TroopRepository troopRepository;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 1000, 1000, java.util.List.of(townhall, mine, barracks));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
        utilityMethods.createTroop(user.getId());
    }

    @AfterEach
    public void after() { utilityMethods.clearDB(); }

    @Test
    public void getTroops() throws Exception {
        mockMvc.perform(get("/kingdom/troops")
               .contentType(MediaType.APPLICATION_JSON)
               .header("Authorization", "Bearer " + token)
                .with(request -> {
                 request.setRemoteAddr(ip);
                 return request;
               })
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.troops", Matchers.any(net.minidev.json.JSONArray.class)));
    }
}
