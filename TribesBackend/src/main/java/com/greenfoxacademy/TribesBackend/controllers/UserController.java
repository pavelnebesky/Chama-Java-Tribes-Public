package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.MissingParamsException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.net.http.HttpClient;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity getUserById(@RequestBody User user, HttpServletRequest request) {
        //try {
        //    userService.checkUserParamsForLogin(user);
        //} catch (FrontendException e) {
        //    return userService.getExceptionService().handleResponseWithException(e);
        //}
        return ResponseEntity.ok(userService.generateTokenBasedOnEmail(user.getEmail(),request));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        //try {
        //    userService.checkUserParamsForReg(user);
        //} catch (FrontendException e) {
        //    return userService.getExceptionService().handleResponseWithException(e);
        //}
        userService.registerUser(user);
        userService.setUserStartingGold(user);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        response.setStatus(200);
    }
}