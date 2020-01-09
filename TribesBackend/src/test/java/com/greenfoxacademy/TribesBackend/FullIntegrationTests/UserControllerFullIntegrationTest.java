package com.greenfoxacademy.TribesBackend.FullIntegrationTests;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ModelMap;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerFullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"something@gmail.com\", \"password\": \"seven\" }"))
                .andExpect(status().isOk());
    }
}

