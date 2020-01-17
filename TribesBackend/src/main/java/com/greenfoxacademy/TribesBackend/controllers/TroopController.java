package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.NotEnoughGoldException;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.services.TroopService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@Getter
@Setter
@RestController
public class TroopController {
    @Autowired
    TroopService troopService;

    @GetMapping("kingdom/troops")
    public ResponseEntity getAllTroops(HttpServletRequest request){
        return ResponseEntity.ok(troopService.getModelMapOfAllTroopsByUserId(request));
    }

    @GetMapping("/kingdom/troops/{troopId}")
    public ResponseEntity getTroopById(HttpServletRequest request, @PathVariable Long troopId){
        return ResponseEntity.ok(troopService.getTroopById(troopId));
    }

    @PostMapping("/kingdom/troops")
    public ResponseEntity createNewTroop(HttpServletRequest request){
        Long userId = troopService.getUtilityService().getIdFromToken(request);
        try {
          return ResponseEntity.ok(troopService.createAndReturnNewTroop(userId)) ;
        }catch (NotEnoughGoldException e){
            return troopService.getUtilityService().handleResponseWithException(e);
        }
    }

    @PutMapping("/kingdom/troops/{troopId}")
    public ResponseEntity trainTroop(HttpServletRequest request, @PathVariable Long troopId, @RequestBody Troop troop){
        try {
            troopService.checksForUpgradeTroop(request, troopId, troop);
        } catch (FrontendException e) {
            return troopService.getUtilityService().handleResponseWithException(e);
        }
        Troop upgradedTroop = troopService.troopLevelUp(troop,troopService.getUserIdFromToken(request));
        return ResponseEntity.status(200).body(upgradedTroop);
    }
}


