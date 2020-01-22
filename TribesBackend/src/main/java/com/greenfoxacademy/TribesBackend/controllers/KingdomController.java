package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        try {
            kingdomService.checkUserId(userId);
        }
        catch (FrontendException e) {
            return kingdomService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok(kingdomService.getKingdomByUserId(userId));
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
