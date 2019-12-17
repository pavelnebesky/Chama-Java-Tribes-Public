package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getUserById(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        String checkResult = userService.checkUserParams(user, response);
        if (checkResult == null) {
            user = userService.findByEmail(user.getEmail());
            return userService.getAuthenticationService().generateJWT(request.getRemoteAddr(), user.getId());
        } else {
            return checkResult;
        }
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        response.setStatus(200);
    }
}