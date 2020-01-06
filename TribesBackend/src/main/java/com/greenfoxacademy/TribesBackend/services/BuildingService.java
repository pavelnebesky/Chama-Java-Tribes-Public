package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.*;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.townhall;
import static com.greenfoxacademy.TribesBackend.enums.resourceType.gold;

@Service
@Getter
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private AuthenticationService authenticationService;

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

    public Iterable<Building> getBuildingsByToken(HttpServletRequest request) {
        return getAllBuildingsByUserId(getAuthenticationService().getIdFromToken(request));
    }

    public Building buildingLevelUp (Building building, int newLevel) {
        int kingdomsGold = building.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int townhallLevel = building.getKingdom().getBuildings().stream().filter(b -> b.getType().equals(townhall)).findAny().get().getLevel();
        int goldToLevelUp = (newLevel - building.getLevel()) * GOLD_TO_LEVEL_UP_BUILDING;
        if (newLevel <= townhallLevel && goldToLevelUp <= kingdomsGold) {
            building.setLevel(newLevel);
            saveBuilding(building);
            Resource resourceToUpdate = building.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
            resourceToUpdate.setAmount(resourceToUpdate.getAmount()-goldToLevelUp);
            resourceRepository.save(resourceToUpdate);
        }
        return building;
    }

    public Building createAndReturnBuilding(long userId, String type) {
        Building newBuilding = new Building();
        newBuilding.setType(BuildingType.valueOf(type));
        newBuilding.setHp(1);
        newBuilding.setKingdom(kingdomRepository.findByUserId(userId));
        newBuilding.setLevel(1);
        newBuilding.setStarted_at(System.currentTimeMillis());
        if (type=="mine") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + MILISECONDS_TO_BUILD_MINE);
        }
        else if (type=="farm") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + MILISECONDS_TO_BUILD_FARM);
        }
        else if (type=="barracks") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + MILISECONDS_TO_BUILD_BARRACKS);
        }
        else if (type=="townhall") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + MILISECONDS_TO_BUILD_TOWNHALL);
        }
        saveBuilding(newBuilding);
        Kingdom kingdomToUpdate = kingdomRepository.findByUserId(userId);
        List<Building> kingdomsBuildings = kingdomToUpdate.getBuildings();
        kingdomsBuildings.add(newBuilding);
        kingdomToUpdate.setBuildings(kingdomsBuildings);
        kingdomRepository.save(kingdomToUpdate);
        return newBuilding;
    }

}
