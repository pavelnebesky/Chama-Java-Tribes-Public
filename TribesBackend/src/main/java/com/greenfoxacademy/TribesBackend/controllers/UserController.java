package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.constants.ExternalLoginConstants;
import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.OAuthCancelledException;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.apache.coyote.Response;
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

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/login/{externalSite}")
    public void createFacebookAuthorization(@PathVariable String externalSite, HttpServletResponse response) {
        try {
            if (externalSite.matches("facebook")) {
                response.sendRedirect(userService.createRedirectionToFacebook());
            } else if (externalSite.matches("google")) {
                response.sendRedirect(userService.createRedirectionToGoogle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/authentication/{externalSite}")
    public ResponseEntity createFacebookAccessToken(@PathVariable String externalSite, @RequestParam("code") String code, HttpServletRequest request) {
        try {
            if (code == null) {
                throw new OAuthCancelledException();
            }
            if (externalSite.matches("facebook")) {
                return ResponseEntity.ok(userService.authenticateFbUser(code, request));
            } else if (externalSite.matches("google")) {
                return ResponseEntity.ok(userService.authenticateGoogleUser(code, request));
            } else {
                //TODO EXCEPTION
                return ResponseEntity.badRequest().body(null);
            }
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