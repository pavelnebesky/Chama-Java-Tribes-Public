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
        if(user==null || user.getEmail()==null || user.getPassword()==null){
            response.setStatus(400);
            if(user==null || (user.getEmail()==null && user.getPassword()==null)){
                return "Missing parameter(s): email, password!";
            }else if(user.getEmail()==null){
                return "Missing parameter(s): email!";
            }else{
                return "Missing parameter(s): password!";
            }
        }else if(!userService.doesUserExistByEmail(user.getEmail())){
            return "No such user: "+user.getEmail()+"!";
        }else if(!userService.doesPasswordMatchAccount(user)){
            return "Wrong password!";
        }else{
            user=userService.findByEmail(user.getEmail());
            return userService.getAuthenticationService().generateJWT(request.getRemoteAddr(),user.getId());
        }
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/logout")
    public void Logout(HttpServletResponse response) {
        response.setStatus(200);
    }
}