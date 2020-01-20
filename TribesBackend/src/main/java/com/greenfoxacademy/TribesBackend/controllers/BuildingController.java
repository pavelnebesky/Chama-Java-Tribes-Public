package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.exceptions.InvalidBuildingTypeException;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.services.BuildingService;
import com.greenfoxacademy.TribesBackend.services.ResourceService;
import com.greenfoxacademy.TribesBackend.services.UserService;
import com.greenfoxacademy.TribesBackend.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.greenfoxacademy.TribesBackend.enums.ResourceType.gold;

@RestController
public class BuildingController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request) {
        return ResponseEntity.ok(buildingService.getMapOfAllBuildingsByToken(request));
    }

    @PostMapping("/kingdom/buildings")
    public ResponseEntity postBuildings(HttpServletRequest request, @RequestBody ModelMap type) {
        try {
            int goldAmount = (userService.findById(utilityService.getIdFromToken(request)))
                    .getKingdom().getResources()
                    .stream().filter(r -> r.getType().equals(gold))
                    .findAny().get().getAmount();
            buildingService.checksForNewBuilding((String) type.getAttribute("type"), goldAmount, utilityService.getIdFromToken(request));
        } catch (FrontendException e) {
            return buildingService.getUtilityService().handleResponseWithException(e);
        } catch (IllegalArgumentException e) {
            return buildingService.getUtilityService().handleResponseWithException(new InvalidBuildingTypeException());
        }
        Long userId = buildingService.getUtilityService().getIdFromToken(request);
        Building newBuilding = buildingService.createAndReturnBuilding(userId, (String) type.getAttribute("type"));
        return ResponseEntity.ok(newBuilding);
    }

    @GetMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity getBuilding(HttpServletRequest request, @PathVariable Long buildingId) {
        try {
            buildingService.checkBuildingId(buildingId);
        } catch (IdNotFoundException e) {
            return buildingService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok(buildingService.getBuildingById(buildingId));
    }

    @PutMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity updateBuilding(HttpServletRequest request, @PathVariable Long buildingId, @RequestBody Building building) {
        try {
            buildingService.checksForUpdateBuilding(buildingId, building);
        } catch (FrontendException e) {
            return buildingService.getUtilityService().handleResponseWithException(e);
        }
        Building updatedBuilding = buildingService.buildingLevelUp(buildingService.getBuildingById(buildingId), building.getLevel());
        return ResponseEntity.status(200).body(updatedBuilding);
    }

    @GetMapping("/leaderboard/buildings")
    public ResponseEntity getBuildingsLeaderboard() {
        return ResponseEntity.ok(buildingService.getLeaderboard());
    }
}
