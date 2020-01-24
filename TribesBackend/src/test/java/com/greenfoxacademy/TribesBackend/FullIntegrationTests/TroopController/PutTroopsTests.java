package com.greenfoxacademy.TribesBackend.FullIntegrationTests.TroopController;

import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
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

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class PutTroopsTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private TroopRepository troopRepository;
    private String token;
    private String ip = "";
    private User user;
    private Troop troop;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 1000, 1000, java.util.List.of(townhall, mine, barracks));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
        troop = utilityMethods.createTroop(user.getId());
    }

    @AfterEach
    public void after() { utilityMethods.clearDB(); }

    @Test
    public void putTroops() throws Exception {
        Long troopId = troopRepository.findTroopById(troop.getId()).getId();
        String content = "{\"level\" : 2 }";
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/troops/" + troopId, "put", token, ip, content))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.level", is(troopRepository.getByIdIsNotNull().getLevel())));
    }

    @Test
    public void putNotEnoughGold() throws Exception {
        Long troopId = troopRepository.findTroopById(troop.getId()).getId();
        Resource gold = resourceRepository.findByType(ResourceType.gold);
        gold.setAmount(0);
        resourceRepository.save(gold);
        FrontendException e = new NotEnoughGoldException();
        String content = "{\"level\" : 2 }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/troops/" + troopId, "put", token, ip, content)), e);
    }

    @Test
    public void putMissingParamsException() throws Exception {
        Long troopId = troopRepository.findTroopById(troop.getId()).getId();
        List<String> missingParams = new ArrayList<String>();
        missingParams.add("level");
        FrontendException e = new MissingParamsException(missingParams);
        String content = "{\"level\" : 0 }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/troops/" + troopId, "put", token, ip, content)), e);
    }

    @Test
    public void putInvalidLevelException() throws Exception {
        Long troopId = troopRepository.findTroopById(troop.getId()).getId();
        FrontendException e = new InvalidLevelException("troop");
        String content = "{}";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/troops/" + troopId, "put", token, ip, content)), e);
    }

    @Test
    public void putIdNotFound() throws Exception {
        Long id = 444L;
        String content = "{\"level\" : 2 }";
        FrontendException e = new IdNotFoundException(id);
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom/troops/" + id, "put", token, ip, content)), e);
    }
}
