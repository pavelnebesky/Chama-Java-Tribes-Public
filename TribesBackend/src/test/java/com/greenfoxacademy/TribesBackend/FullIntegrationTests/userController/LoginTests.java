package com.greenfoxacademy.TribesBackend.FullIntegrationTests.userController;

import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
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

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class LoginTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;

    @BeforeEach
    public void before() {
        utilityMethods.clearDB();
    }

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void successfullLoginTest() throws Exception {
        String password="blah";
        String username="some@email.com";
        utilityMethods.createUser(username, password, true);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"" + username + "\", \"password\": \""+password+"\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.token", Matchers.any(String.class)));
    }

    @Test
    public void missingParamsTest() throws Exception {
        FrontendException e=new MissingParamsException(List.of("username", "password"));
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
        e=new MissingParamsException(List.of("password"));
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"blah.blah\" }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void noSuchEmailTest() throws Exception {
        String username="blah";
        FrontendException e=new NoSuchEmailException(username);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"" + username + "\", \"password\": \"seven\" }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void incorrectPasswordTest() throws Exception {
        String password="blah";
        String username="some@email.com";
        utilityMethods.createUser(username, password, true);
        FrontendException e=new IncorrectPasswordException();
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"" + username + "\", \"password\": \"seven\" }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void emailNotVerifiedTest() throws Exception {
        String password="blah";
        String username="some@email.com";
        utilityMethods.createUser(username, password, false);
        FrontendException e=new EmailNotVerifiedException();
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"" + username + "\", \"password\": \"seven\" }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }
}
