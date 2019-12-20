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
        ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getBuildingsByToken(request));
        return ResponseEntity.ok(modelMap);
    }

    @PostMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request, @RequestBody Building building) {
        //TODO: TEST
        //String loudScreaming = jsonType.getJSONObject("LabelData").getString("slogan");
        String buildingType = building.getType().toString();
        Long userId = buildingService.getAuthenticationService().getIdFromToken(request);
        Building newBuilding = buildingService.createAndReturnBuilding( userId, buildingType);
        return ResponseEntity.ok(newBuilding);
    }

    @GetMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity getBuilding(HttpServletRequest request, @PathVariable long buildingId) {
        //TODO: TEST
       return ResponseEntity.ok(buildingService.getBuildingById(buildingId));
//        } else {
//            ModelMap modelMap = new ModelMap().addAttribute("status", "error")
//                    .addAttribute("message", buildingId + " not found");
//            return ResponseEntity.status(404).body(modelMap);
//        }
    }

    @PutMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity updateBuilding(HttpServletRequest request, @PathVariable long buildingId, @RequestBody Building building) {
        //TODO: TEST
        Building updateBuilding = buildingService.getBuildingById(buildingId);
        updateBuilding.setLevel(building.getLevel());
        buildingService.saveBuilding(updateBuilding);
        return ResponseEntity.status(200).body(updateBuilding);
    }
}
