package com.greenfoxacademy.TribesBackend.FullIntegrationTests.buildingController;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
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

import java.util.List;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.mine;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.townhall;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class GetBuildingsTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    @Autowired
    private BuildingRepository buildingRepository;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 1000, 1000, List.of(townhall, mine));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
    }

    @AfterEach
    public void after() { utilityMethods.clearDB();
    }

    @Test
    public void getBuildings() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.buildings", Matchers.any(List.class)))
                .andExpect(jsonPath("$.buildings[0].type", is("townhall")))
                .andExpect(jsonPath("$.buildings[1].type", is("mine")));
    }

    @Test
    public void getSpecifiedBuilding() throws Exception {
        Long townhallId = ((List<Building>) buildingRepository.findAll())
                .stream().filter(b -> b.getType().equals(townhall)).findAny().get().getId();
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings/" + townhallId, "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("townhall")))
                .andExpect(jsonPath("$.level", is(1)));
    }

    @Test
    public void getNotExistingBuilding() throws Exception {
        Long id = 99339933L;
        FrontendException e = new IdNotFoundException(id);
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings/" + id, "get", token, ip, "{}")), e);
    }

    @Test
    public void getLeaderboard() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/leaderboard/buildings", "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.leaderboard", Matchers.any(List.class)))
                .andExpect(jsonPath("$.leaderboard[0].kingdomname", is("Johns kingdom")))
                .andExpect(jsonPath("$.leaderboard[0].buildings", is(2)));
    }

}
