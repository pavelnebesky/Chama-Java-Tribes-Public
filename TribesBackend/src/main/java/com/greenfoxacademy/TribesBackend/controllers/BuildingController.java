package com.greenfoxacademy.TribesBackend.controllers;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ID_CLAIM;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.TOKEN_PREFIX;

@RestController
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request) {
        //TODO: TEST
        ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getAllBuildingsByUserId(buildingService.getAuthenticationService().getIdFromToken(request)));
        return ResponseEntity.ok(modelMap);
    }

    @PostMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request, @RequestBody Building building) {
        //TODO: TEST
        //String loudScreaming = jsonType.getJSONObject("LabelData").getString("slogan");
        String type = building.getType().toString();
        Building newBuilding = buildingService.createAndReturnBuilding(buildingService.getAuthenticationService().getIdFromToken(request), building.getType().toString());
        return ResponseEntity.ok(newBuilding);
    }

    @GetMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity getBuilding(HttpServletRequest request, @PathVariable long buildingId) {
        //TODO: TEST
        if (buildingService.getAuthenticationService().getIdFromToken(request) == buildingService.getBuildingById(buildingId).getKingdom().getUser().getId()) {
            ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getBuildingById(buildingId));
            return ResponseEntity.ok(modelMap);
        } else {
            ModelMap modelMap = new ModelMap().addAttribute("status", "error")
                    .addAttribute("message", buildingId + " not found");
            return ResponseEntity.status(404).body(modelMap);
        }
    }

    @PutMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity updateBuilding(HttpServletRequest request, @PathVariable long buildingId, @RequestBody int level) {
        //TODO: TEST
        Building updateBuilding = buildingService.getBuildingById(buildingId);
        updateBuilding.setLevel(level);
        buildingService.saveBuilding(updateBuilding);
        return ResponseEntity.status(200).body(updateBuilding);
    }
}
