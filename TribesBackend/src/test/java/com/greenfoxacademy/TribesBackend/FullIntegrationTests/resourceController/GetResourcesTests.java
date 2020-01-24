package com.greenfoxacademy.TribesBackend.FullIntegrationTests.resourceController;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.exceptions.ParameterNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Matches;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class GetResourcesTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    @Autowired
    private BuildingRepository buildingRepository;
    private String token;
    private String ip = "";
    private User user;
    private Kingdom kingdom;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("something@sth.com", "kingdomName", 10000, 10000, List.of(BuildingType.townhall, BuildingType.barracks, BuildingType.mine, BuildingType.farm));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void getResources() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/resources", "get", token, ip,
                "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resources", Matchers.any(List.class)))
                .andExpect(jsonPath("$.resources[0].type", is("gold")))
                .andExpect(jsonPath("$.resources[0].amount", is(10000)))
                .andExpect(jsonPath("$.resources[1].type", is("food")))
                .andExpect(jsonPath("$.resources[1].amount", is(10000)));
    }

    @Test
    public void getResourceType() throws Exception {
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/resources/gold", "get", token, ip, "{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("gold")))
                .andExpect(jsonPath("$.amount", is(10000)));
    }

    @Test
    public void getWrongResourceType() throws Exception {
        FrontendException e = new ParameterNotFoundException("mercury");
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods
                .buildAuthRequest("/kingdom/resources/mercury", "get", token, ip, "{}")), e);
    }
}
