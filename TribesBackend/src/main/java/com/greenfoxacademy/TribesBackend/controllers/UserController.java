package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.net.http.HttpClient;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) {

        User user = userService.findById(userId);
            return user;
        }

     @PostMapping("/register")
     public User registerUser (@RequestBody User user){
     return userService.save(user);
    }

    @GetMapping("/logout")
    public void  Logout(HttpServletResponse response){
       response.setStatus(200);
    }
}