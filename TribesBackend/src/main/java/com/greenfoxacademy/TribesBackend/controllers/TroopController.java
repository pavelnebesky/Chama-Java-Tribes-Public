package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.NotEnoughGoldException;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.services.TroopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TroopController {
    @Autowired
    TroopService troopService;

    @GetMapping("kingdom/troops")
    public ResponseEntity getAllTroops(HttpServletRequest request){
        return ResponseEntity.ok(troopService.getModelMapOfAllTroopsByUserId(request));
    }

    @GetMapping("/kingdom/troops/[troopId]")
    public ResponseEntity getTrooperById(HttpServletRequest request, long troopId){
        return ResponseEntity.ok(troopService.getTrooperById(troopId));
    }

    @PostMapping("/kingdom/troops")
    public ResponseEntity createNewTrooper(HttpServletRequest request){
        Long userId = troopService.getUtilityService().getIdFromToken(request);
        try {
            newTroop = troopService.createAndReturnNewTroop(userId);
        }catch (NotEnoughGoldException e){
            return troopService.getUtilityService().
        }

    }
}
