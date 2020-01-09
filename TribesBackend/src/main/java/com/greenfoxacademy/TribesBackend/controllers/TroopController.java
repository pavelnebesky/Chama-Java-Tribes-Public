package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.services.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TroopController {

    @Autowired
    TroopService troopService;
    @GetMapping("kingdom/troops")
    public ResponseEntity getTroopsin(HttpServletRequest request){
        return ResponseEntity.ok(troopService.getModelMapOfAllTroopsByUserId(request));
    }
}
