package com.greenfoxacademy.TribesBackend.controllers;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ID_CLAIM;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.TOKEN_PREFIX;

@RestController
public class KingdomController {

    @Autowired
    private KingdomService kingdomService;

    @GetMapping("/kingdom")
    public ResponseEntity getKingdom(HttpServletRequest request) {
        //TODO: TEST
        Long userId = kingdomService.getUtilityService().getIdFromToken(request);
        return ResponseEntity.ok(kingdomService.getKingdomByUserId(userId));
    }

    @GetMapping("kingdom/{userId}")
    public ResponseEntity getKingdomByUserId(@PathVariable long userId) {
        //TODO: TEST
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        if (kingdom != null) {
            return ResponseEntity.ok(kingdom);
        } else {
            ModelMap modelMap = new ModelMap().addAttribute("status", "error")
                    .addAttribute("message", "UserId not found");
            return ResponseEntity.status(404).body(modelMap);
        }
    }

    @PutMapping("/kingdom")
    public ResponseEntity updateKingdom(@RequestBody ModelMap kingdomDataToUpdate, HttpServletRequest request) {
        //TODO: TEST
        Long userId = kingdomService.getUtilityService().getIdFromToken(request);
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        kingdomService.updateKingdom(kingdom, kingdomDataToUpdate);
        return ResponseEntity.ok(kingdom);
    }
}
