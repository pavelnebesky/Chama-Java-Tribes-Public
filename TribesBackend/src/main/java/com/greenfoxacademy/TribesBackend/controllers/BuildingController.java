package com.greenfoxacademy.TribesBackend.controllers;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.ID_CLAIM;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.TOKEN_PREFIX;

@RestController
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request){
        //TODO: TEST
        String token = request.getHeader("Authorization").replace(TOKEN_PREFIX, "");
        String userId = JWT.decode(token).getClaim(ID_CLAIM).asString();
        ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getAllBuildingsByUserId(Long.parseLong(userId)))
        return ResponseEntity.ok (modelMap);
    }

    @PostMapping("/kingdom/buildings")
    public ResponseEntity getBuildings(HttpServletRequest request, @RequestBody String type){
        //TODO: TEST
        String token = request.getHeader("Authorization").replace(TOKEN_PREFIX, "");
        long userId = Long.parseLong(JWT.decode(token).getClaim(ID_CLAIM).asString());

        buildingService.createBuilding(userId, type);
        ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getAllBuildingsByUserId(Long.parseLong(userId)))
        return ResponseEntity.ok (modelMap);
    }

    @GetMapping("/kingdom/buildings/{buildingId}")
    public ResponseEntity getBuildings(HttpServletRequest request, @PathVariable long buildingId){
        //TODO: TEST
        String token = request.getHeader("Authorization").replace(TOKEN_PREFIX, "");
        String userId = JWT.decode(token).getClaim(ID_CLAIM).asString();
        if (Long.parseLong(userId)==buildingService.getBuildingById(buildingId).getKingdom().getUser().getId())
        {
            ModelMap modelMap = new ModelMap().addAttribute("buildings", buildingService.getBuildingById(buildingId))
            return ResponseEntity.ok (modelMap);
        }
        else
        {
            ModelMap modelMap = new ModelMap().addAttribute("status", "error")
                    .addAttribute("message",buildingId + " not found");
            return ResponseEntity.status(404).body(modelMap);
        }

    }

//    public Kingdom getKingdom(HttpServletRequest request) {
//        //TODO: TEST
//        String token = request.getHeader("Authorization").replace(TOKEN_PREFIX, "");
//        String userId = JWT.decode(token).getClaim(ID_CLAIM).asString();
//        return kingdomService.getKingdomByUserId(Long.parseLong(userId));
//    }

        @GetMapping("kingdom/{userId}")
        public ResponseEntity getKingdomByUserId(@PathVariable long userId) {
            //TODO: TEST
            Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
            if (kingdom != null) {
                return ResponseEntity.ok(kingdom);
            }
            else {
                ModelMap modelMap = new ModelMap().addAttribute("status", "error")
                        .addAttribute("message","UserId not found");
                return ResponseEntity.status(404).body(modelMap);
            }
}