package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroopController {

    @Autowired
    TroopService troopService;
    @GetMapping("kingdom/troops")
    public ResponseEntity getTroops(Kingdom kingdom){
        return ResponseEntity.ok(troopService.getAllTroopsByKingdom(kingdom));
    }
}
