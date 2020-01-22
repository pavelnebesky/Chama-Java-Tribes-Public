package com.greenfoxacademy.TribesBackend.FullIntegrationTests.buildingController;

import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
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

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class PostBuildingTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    private String token;
    private String ip = "";
    private User user;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 250, 0, List.of(townhall, mine));
        token = utilityMethods.generateToken("john@doe.com", ip, user.getId());
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void postBarracks() throws Exception {
        String content = "{ \"type\" : \"barracks\" }";
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "post", token, ip, content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("barracks")))
                .andExpect(jsonPath("$.level", is(1)));
    }

    @Test
    public void postMissingBuildingType() throws Exception {
        FrontendException e = new MissingParamsException(List.of("type"));
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "post", token, ip, "{}")), e);
    }

    @Test
    public void postTownhallAlreadyExists() throws Exception {
        FrontendException e = new TownhallAlreadyExistsException();
        String content = "{ \"type\" : \"townhall\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "post", token, ip, content)), e);
    }

    @Test
    public void postNotEnoughGold() throws Exception {
        Resource gold = resourceRepository.findByType(ResourceType.gold);
        gold.setAmount(0);
        resourceRepository.save(gold);
        FrontendException e = new NotEnoughGoldException();
        String content = "{ \"type\" : \"barracks\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "post", token, ip, content)), e);
    }

    @Test
    public void postTownhallFirst() throws Exception {
        utilityMethods.clearDB();
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 250, 0, List.of());
        token = utilityMethods.generateToken("john@doe.com", ip, user.getId());
        FrontendException e = new TownhallFirstException();
        String content = "{ \"type\" : \"barracks\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/buildings", "post", token, ip, content)), e);
    }
}
