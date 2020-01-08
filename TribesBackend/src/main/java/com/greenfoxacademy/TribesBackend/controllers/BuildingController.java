package com.greenfoxacademy.TribesBackend.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request) {
        //TODO: TEST
        //TODO: ERRORS
        return ResponseEntity.ok(buildingService.getMapOfAllBuildingsByToken(request));
    }

    @PostMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request, @RequestBody Building building) {
        //TODO: TEST
        //TODO: ERRORS
        //String loudScreaming = jsonType.getJSONObject("LabelData").getString("slogan");
        String buildingType = building.getType().toString();
        Long userId = buildingService.getUtilityService().getIdFromToken(request);
        Building newBuilding = buildingService.createAndReturnBuilding(userId, buildingType);
        return ResponseEntity.ok(newBuilding);
    }

    @GetMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity getBuilding(HttpServletRequest request, @PathVariable long buildingId) {
        //TODO: TEST
        //TODO: ERRORS
        return ResponseEntity.ok(buildingService.getBuildingById(buildingId));
    }

    @PutMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity updateBuilding(HttpServletRequest request, @PathVariable long buildingId, @RequestBody Building building) {
        //TODO: TEST
        //TODO: ERRORS
        Building updatedBuilding = buildingService.buildingLevelUp(buildingService.getBuildingById(buildingId), building.getLevel());
        return ResponseEntity.status(200).body(updatedBuilding);
    }

    @GetMapping("/leaderboard/buildings/")
    public ResponseEntity getBuildingsLeaderboard() {
        //TODO: TEST
        //TODO: ERRORS
        return ResponseEntity.ok(buildingService.getLeaderboard());
    }
}
