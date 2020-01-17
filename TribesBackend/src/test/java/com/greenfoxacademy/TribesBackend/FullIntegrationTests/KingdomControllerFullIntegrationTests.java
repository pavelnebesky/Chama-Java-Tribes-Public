package com.greenfoxacademy.TribesBackend.FullIntegrationTests;

import com.greenfoxacademy.TribesBackend.models.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class KingdomControllerFullIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @Before
    public void SetUp() {
        user = new User();
    }

    @Test
    public void givenBadUserId_whenGetKingdomByUserId_thenReturnUserNotFoundException() throws Exception {
        RequestBuilder request = get("/kingdom").pa
        mockMvc.perform(get("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"something@gmail.com\", \"password\": \"seven\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username", is("something@gmail.com")))
                .andExpect(jsonPath("$.kingdom", is("something's kingdom")));
    }
}
