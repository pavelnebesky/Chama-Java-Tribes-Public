package com.greenfoxacademy.TribesBackend.FullIntegrationTests.TroopController;

import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class PutTroopsTests {

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
    private Troop troop;

    @BeforeEach
    public void before() {
        user = utilityMethods.createEverything("john@doe.com", "Johns kingdom", 1000, 1000, java.util.List.of(townhall, mine, barracks));
        token = utilityMethods.generateToken("something@sth.com", ip, user.getId());
        troop = utilityMethods.createTroop(user.getId());
    }

    @AfterEach
    public void after() { utilityMethods.clearDB(); }

}
