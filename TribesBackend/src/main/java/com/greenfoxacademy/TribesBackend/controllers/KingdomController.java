package com.greenfoxacademy.TribesBackend.controllers;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ID_CLAIM;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.TOKEN_PREFIX;

@RestController
public class KingdomController {

    @Autowired
    KingdomService kingdomService;

    @GetMapping("/kingdom")
    public Kingdom getKingdom(HttpServletRequest request) {
        //TODO: TEST
        String token = request.getHeader("Authorization").replace(TOKEN_PREFIX, "");
        String userId = JWT.decode(token).getClaim(ID_CLAIM).asString();
        return kingdomService.getKingdomByUserId(Long.parseLong(userId));
    }

    @GetMapping("kingdom/{userId}")
    public ResponseEntity getKingdomByUserId(@PathVariable long userId) {
        //TODO: TEST
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        if (kingdom != null) {
            return ResponseEntity.ok(kingdom);
        }
        else {
            ModelMap modelMap = new ModelMap().addAttribute("status", "error")
                                              .addAttribute("message","UserId not found");
            return ResponseEntity.status(404).body(modelMap);
        }
    }
}
