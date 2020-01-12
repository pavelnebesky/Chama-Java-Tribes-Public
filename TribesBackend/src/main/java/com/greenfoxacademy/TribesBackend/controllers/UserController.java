package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity getUserById(@RequestBody User user, HttpServletRequest request) {
        try {
            userService.checkUserParamsForLogin(user);
        } catch (FrontendException e) {
            return userService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok(userService.createLoginResponse(user, request));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody ModelMap modelMap) {
        try {
            userService.checkUserParamsForReg(userService.getUserFromModelMap(modelMap));
        } catch (FrontendException e) {
            return userService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok(userService.registerUser(userService.getUserFromModelMap(modelMap), (String) modelMap.getAttribute("kingdom")));
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        response.setStatus(200);
    }

    @GetMapping("/facebook/login")
    public void createFacebookAuthorization(HttpServletResponse response) {
        try {
            response.sendRedirect(userService.createRedirectionToFacebook());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/facebook/authentication")
    public ResponseEntity createFacebookAccessToken(@RequestParam("code") String code, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.authenticateFbUser(code, request));
        } catch (FrontendException e) {
            return userService.getUtilityService().handleResponseWithException(e);
        }
    }

    @GetMapping("/verify/{verCode}")
    public ResponseEntity verify(@PathVariable String verCode) {
        try {
            userService.verifyEmail(verCode);
        } catch (FrontendException e) {
            return userService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok().body("email verified!");
    }
}