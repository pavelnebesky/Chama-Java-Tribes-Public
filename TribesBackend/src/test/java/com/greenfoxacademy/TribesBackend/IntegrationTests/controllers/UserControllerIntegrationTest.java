package com.greenfoxacademy.TribesBackend.IntegrationTests.controllers;

import com.greenfoxacademy.TribesBackend.controllers.UserController;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.MissingParamsException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;

import static org.hamcrest.core.Is.is;

import com.greenfoxacademy.TribesBackend.services.UtilityService;
import org.hibernate.type.AnyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UtilityService utilityService;
    @MockBean
    private UserService userService;

    @Test
    @Disabled
    public void givenNewCorrectUser_whenRegisterUser_thenReturnUserModelMap() throws Exception {

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("id", "1");
        modelMap.addAttribute("username", "something@gmail.com");
        modelMap.addAttribute("kingdom", "something's kingdom");

        given(userService.getUserFromModelMap(any(ModelMap.class))).willReturn(new User());
        given(userService.registerUser(any(User.class), any(String.class))).willReturn(modelMap);


        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"something@gmail.com\", \"password\": \"seven\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.username", is("something@gmail.com")))
                .andExpect(jsonPath("$.kingdom", is("something's kingdom")));

    }

    @Test
    @Disabled
    public void givenWrongUser_whenCheckUserParamsForReg_thenReturnMissingParamException() throws Exception {
        FrontendException exception = new MissingParamsException(Arrays.asList("password"));

        ModelMap exceptionModelMap = new ModelMap();
        exceptionModelMap.addAttribute("status", "error");
        exceptionModelMap.addAttribute("error", exception.getMessage());

        doThrow(exception)
                .when(userService).checkUserParamsForReg(any(User.class));
        when(utilityService.handleResponseWithException(any(FrontendException.class)))
                .thenReturn(ResponseEntity.status(400).body(exceptionModelMap));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"something@gmail.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is("error")))
                .andExpect(jsonPath("error", is(exception.getMessage())));
    }
}
