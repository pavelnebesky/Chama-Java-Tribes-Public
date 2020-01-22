package com.greenfoxacademy.TribesBackend.FullIntegrationTests.userController;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class LogoutTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;
    private String token;
    private String ip = "";

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void successfullLogoutTest() throws Exception {
        User user = utilityMethods.createEverything("some@email.com", "blah", 0, 0, List.of());
        token = utilityMethods.generateToken(user.getUsername(), ip, user.getId());
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom", "get", token, ip, "{}"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(utilityMethods.buildAuthRequest("/logout", "post", token, ip, "{}"))
                .andExpect(status().isOk());
        mockMvc.perform(utilityMethods.buildAuthRequest("/kingdom", "get", token, ip, "{}"))
                .andExpect(status().is(401));
    }
}
