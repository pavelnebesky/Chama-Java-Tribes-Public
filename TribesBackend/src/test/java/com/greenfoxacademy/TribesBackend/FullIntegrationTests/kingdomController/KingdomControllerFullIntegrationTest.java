package com.greenfoxacademy.TribesBackend.FullIntegrationTests.kingdomController;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
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

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class KingdomControllerFullIntegrationTest {

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
    private Kingdom kingdom;

    @BeforeEach
    public void setup() {
        user = utilityMethods.createEverything("something@sth.com", "kingdomName", 10000, 10000, List.of(BuildingType.barracks, BuildingType.townhall));
        kingdom = user.getKingdom();
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
    public void whenGetKingdom_thenReturnUsersKingdom() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom", "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(kingdom.getId().intValue())))
                .andExpect(jsonPath("name", is("kingdomName")))
                .andExpect(jsonPath("userId", is(user.getId().intValue())));
    }

    @Test
    public void whenGettingCorrectUserId_thenReturnUsersKingdom() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/" + user.getId().intValue(),
                "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(kingdom.getId().intValue())))
                .andExpect(jsonPath("name", is("kingdomName")))
                .andExpect(jsonPath("userId", is(user.getId().intValue())));
    }

    @Test
    public void whenGettingInorrectUserId_thenReturnIdNotFoundException() throws Exception {
        FrontendException e = new IdNotFoundException(14762587164L);
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/" + 14762587164L,
                "get", token, ip, "{}")), e);
    }

    @Test
    public void whenRecievedNewDataForKingdom_thenReturnStatusOkandNewUpdatedKingdom() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom", "put", token, ip,
                "{\"name\" : \"MI5\", \"locationX\" : 4, \"locationY\" : 12}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(kingdom.getId().intValue())))
                .andExpect(jsonPath("name", is("MI5")))
                .andExpect(jsonPath("userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$.location.x", is(4)))
                .andExpect(jsonPath("$.location.y", is(12)));
    }
}

