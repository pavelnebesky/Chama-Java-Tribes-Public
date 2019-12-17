package com.greenfoxacademy.TribesBackend.controllers;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ID_CLAIM;

@RestController
public class KingdomController {

    @Autowired
    KingdomService kingdomService;

    @GetMapping("/kingdom")
    public Kingdom getKingdom(HttpServletRequest request){
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String userId = JWT.decode(token).getClaim(ID_CLAIM).asString();
        return kingdomService.getKingdomByUserId(userId);
    }

}
