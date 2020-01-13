package com.greenfoxacademy.TribesBackend.FullIntegrationTests;

import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class UserControllerFullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenNewCorrectUser_whenRegisterUser_thenReturnUserModelMap() throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"something@gmail.com\", \"password\": \"seven\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username", is("something@gmail.com")))
                .andExpect(jsonPath("$.kingdom", is("something's kingdom")));
    }
}

