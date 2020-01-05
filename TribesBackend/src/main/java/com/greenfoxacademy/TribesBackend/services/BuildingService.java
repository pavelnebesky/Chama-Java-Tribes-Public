package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private AuthenticationService authenticationService;

    private static final int milisecondsToBuildMine  = 10000;
    private static final int milisecondsToBuildFarm  = 10000;
    private static final int milisecondsToBuildBarracks  = 10000;

    public Building saveBuilding (Building building) {
        buildingRepository.save(building);
        return building;
    }

    public Iterable<Building> getAllBuildingsByUserId(long userId) {
        return buildingRepository.findAllByKingdomUserId(userId);
    }

    public Building getBuildingById(long buildingId) {
        return buildingRepository.findById(buildingId).get();
    }

    public Iterable<Building> getBuildingsByToken(HttpServletRequest request)
    {
        return getAllBuildingsByUserId(getAuthenticationService().getIdFromToken(request));
    }

    public Map<String, Integer> getLeaderboard()
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (Kingdom kingdom:kingdomRepository.findAll()
             ) {
            map.put(kingdom.getName(), kingdom.getBuildings().size());
        }
        return map;
    }

    public Building createAndReturnBuilding(long userId, String type) {
        Building newBuilding = new Building();
        newBuilding.setType(BuildingType.valueOf(type));
        newBuilding.setHp(1);
        newBuilding.setKingdom(kingdomRepository.findByUserId(userId));
        newBuilding.setLevel(1);
        newBuilding.setStarted_at(System.currentTimeMillis());
        if (type=="mine") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + milisecondsToBuildMine);
        }
        else if (type=="farm") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + milisecondsToBuildFarm);
        }
        else if (type=="barracks") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + milisecondsToBuildBarracks);
        }
        saveBuilding(newBuilding);
        return newBuilding;
    }

}
