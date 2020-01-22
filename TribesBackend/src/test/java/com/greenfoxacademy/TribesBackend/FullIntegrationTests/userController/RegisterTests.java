package com.greenfoxacademy.TribesBackend.FullIntegrationTests.userController;

import com.greenfoxacademy.TribesBackend.exceptions.EmailAlreadyTakenException;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.MissingParamsException;
import com.greenfoxacademy.TribesBackend.exceptions.NotValidEmailException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class RegisterTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void successfullRegisterTest() throws Exception {
        String username = "some@email.com";
        String kingdomName = "some's kingdom";
        String content = "{ \"username\" : \"" + username + "\", \"password\" : \"blah\" }";
        mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.kingdom", is(kingdomName)));
    }

    @Test
    public void kingdomNameTest() throws Exception {
        String username = "some@email.com";
        String kingdomName = "random kingdom name";
        String content = "{ \"username\" : \"" + username + "\", \"password\" : \"blah\", \"kingdom\" : \"" + kingdomName + "\" }";
        mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.kingdom", is(kingdomName)));
    }

    @Test
    public void usernameAlreadyTakenTest() throws Exception {
        String username = "some@email.com";
        String content = "{ \"username\" : \"" + username + "\", \"password\" : \"blah\" }";
        User user = utilityMethods.createUser(username, "blah", true);
        FrontendException e = new EmailAlreadyTakenException(user.getUsername());
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content)), e);
    }

    @Test
    public void notValidEmailTest() throws Exception {
        String content = "{ \"username\" : \"blah\", \"password\" : \"blah\" }";
        FrontendException e = new NotValidEmailException();
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content)), e);
        content = "{ \"username\" : \"blah@blah\", \"password\" : \"blah\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content)), e);
        content = "{ \"username\" : \"blah.blah\", \"password\" : \"blah\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content)), e);
    }

    @Test
    public void missingParamsTest() throws Exception {
        FrontendException e = new MissingParamsException(List.of("username", "password"));
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", "{}")), e);
        e = new MissingParamsException(List.of("password"));
        String content = "{ \"username\": \"blah.blah\" }";
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/register", "post", content)), e);
    }
}
