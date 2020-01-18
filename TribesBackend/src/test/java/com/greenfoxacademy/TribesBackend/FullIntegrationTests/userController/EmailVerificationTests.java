package com.greenfoxacademy.TribesBackend.FullIntegrationTests.userController;

import com.greenfoxacademy.TribesBackend.exceptions.EmailAlreadyVerifiedException;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IncorrectVerCodeException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.utilityMethods.UtilityMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-testing.properties")
public class EmailVerificationTests {

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
    public void emailAlreadyVerifiedTest() throws Exception{
        User user=utilityMethods.createUser("blah@blah.blah","",true);
        FrontendException e = new EmailAlreadyVerifiedException();
        mockMvc.perform(get("/verify/"+user.getVerificationCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }

    @Test
    public void incorrectVerificationCodeTest() throws Exception{
        utilityMethods.createUser("blah@blah.blah","",true);
        FrontendException e = new IncorrectVerCodeException();
        mockMvc.perform(get("/verify/blah")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ }"))
                .andExpect(status().is(e.getSc()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.error", is(e.getMessage())));
    }
}
