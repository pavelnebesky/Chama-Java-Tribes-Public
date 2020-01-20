package com.greenfoxacademy.TribesBackend.FullIntegrationTests.buildingController;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.hamcrest.Matchers;
import org.json.JSONArray;
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
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")

public class PutBuildingTests {

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
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 200, 0, List.of(townhall, mine));
        token = utilityMethods.generateToken("john@doe.com", ip, user.getId());
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void putUpdateTownhallLevel() throws Exception {
        Long townhallId = ((List<Building>) buildingRepository.findAll())
                .stream().filter(b -> b.getType().equals(townhall)).findAny().get().getId();
        mockMvc.perform(put("/kingdom/buildings/" + townhallId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ \"level\" : 2 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("townhall")))
                .andExpect(jsonPath("$.level", is(2)));
    }

    @Test
    public void putInvalidLevel() throws Exception {
        Long townhallId = ((List<Building>) buildingRepository.findAll())
                .stream().filter(b -> b.getType().equals(townhall)).findAny().get().getId();
        FrontendException e = new InvalidLevelException("building");
        mockMvc.perform(put("/kingdom/buildings/" + townhallId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ \"level\" : 1 }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void putNotExistingBuilding() throws Exception {
        Long id = 99339933L;
        FrontendException e = new IdNotFoundException(id);
        mockMvc.perform(get("/kingdom/buildings/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ \"level\" : 2 }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void putTownhallLevelTooLow() throws Exception {
        Long townhallId = ((List<Building>) buildingRepository.findAll())
                .stream().filter(b -> b.getType().equals(mine)).findAny().get().getId();
        FrontendException e = new TownhallLevelTooLowException();
        mockMvc.perform(put("/kingdom/buildings/" + townhallId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ \"level\" : 2 }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void putNotEnoughGold() throws Exception {
        Resource gold = resourceRepository.findByType(ResourceType.gold);
        gold.setAmount(0);
        resourceRepository.save(gold);
        FrontendException e = new NotEnoughGoldException();
        Long townhallId = ((List<Building>) buildingRepository.findAll())
                .stream().filter(b -> b.getType().equals(townhall)).findAny().get().getId();
        mockMvc.perform(put("/kingdom/buildings/" + townhallId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{ \"level\" : 2 }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }
}
