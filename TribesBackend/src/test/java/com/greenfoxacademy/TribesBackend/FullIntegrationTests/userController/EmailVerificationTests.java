package com.greenfoxacademy.TribesBackend.FullIntegrationTests.userController;

import com.greenfoxacademy.TribesBackend.exceptions.EmailAlreadyVerifiedException;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IncorrectVerCodeException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.testUtilities.UtilityMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-testing.properties")
public class EmailVerificationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilityMethods utilityMethods;

    @AfterEach
    public void after() {
        utilityMethods.clearDB();
    }

    @Test
    public void emailAlreadyVerifiedTest() throws Exception {
        User user = utilityMethods.createUser("blah@blah.blah", "", true);
        FrontendException e = new EmailAlreadyVerifiedException();
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/verify/" + user.getVerificationCode(), "get", "{}")), e);
    }

    @Test
    public void incorrectVerificationCodeTest() throws Exception {
        utilityMethods.createUser("blah@blah.blah", "", true);
        FrontendException e = new IncorrectVerCodeException();
        utilityMethods.exceptionExpectations(mockMvc.perform(utilityMethods.buildNonAuthRequest("/verify/blah", "get", "{}")), e);
    }
}
