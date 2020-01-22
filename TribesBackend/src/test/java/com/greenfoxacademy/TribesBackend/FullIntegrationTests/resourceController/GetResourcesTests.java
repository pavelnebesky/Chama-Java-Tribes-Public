package com.greenfoxacademy.TribesBackend.FullIntegrationTests.resourceController;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.mine;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.townhall;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("frantisek@dobrota.com", "frantisek's kingdom", 1000, 1000, List.of(townhall, mine));
        token = utilityMethods.generateToken("frantisek@dobrota.com", ip, user.getId());
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void getBuildings() throws Exception {
        mockMvc.perform(get("/kingdom/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
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
        mockMvc.perform(get("/kingdom/buildings/" + townhallId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("townhall")))
                .andExpect(jsonPath("$.level", is(1)));
    }

    @Test
    public void getNotExistingBuilding() throws Exception {
        Long id = 99339933L;
        FrontendException e = new IdNotFoundException(id);
        mockMvc.perform(get("/kingdom/buildings/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void getLeaderboard() throws Exception {
        mockMvc.perform(get("/leaderboard/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .with(request -> {
                    request.setRemoteAddr(ip);
                    return request;
                })
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.leaderboard", Matchers.any(List.class)))
                .andExpect(jsonPath("$.leaderboard[0].kingdomname", is("Johns kingdom")))
                .andExpect(jsonPath("$.leaderboard[0].buildings", is(2)));
    }
}
