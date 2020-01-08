package com.greenfoxacademy.TribesBackend.IntegrationTests.controllers;

import com.greenfoxacademy.TribesBackend.controllers.UserController;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;

import java.nio.charset.Charset;

import static javax.swing.UIManager.get;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void givenNewCorrectUser_whenRegisterUser_thenReturnUserModelMap() throws Exception {
        User user = new User();
        user.setEmail("test@test.cz");
        user.setPassword("awudhakuhwd");
        user.setKingdom(new Kingdom());
        user.getKingdom().setName("testKingdom");

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("id", user.getId());
        modelMap.addAttribute("email", user.getEmail());
        modelMap.addAttribute("kingdom", user.getKingdom().getName());

        given(userService.registerUser(user)).willReturn(modelMap);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/register")
                .content("{\"email\": \"test@test.cz\", \"password\": \"awudhakuhwd\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("User is added")));
    }
}
