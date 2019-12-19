package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    private KingdomRepository kingdomRepository;
    private static final int secondsToBuildMine  = 10;
    private static final int secondsToBuildFarm  = 10;
    private static final int secondsToBuildBarracks  = 10;

    public void saveBuilding (Building building) {
        buildingRepository.save(building);
    }

    public Iterable<Building> getAllBuildingsByUserId(long userId) {
        return buildingRepository.findAllByKingdomUserId(userId);
    }

    public Building getBuildingById(long buildingId) {
        return buildingRepository.findBuildingById(buildingId);
    }

    public void createBuilding(long userId, String type) {
        Building newBuilding = new Building();
        if (type=="mine") {
            newBuilding.setType(BuildingType.mine);
        }
        else if (type=="farm") {
            newBuilding.setType(BuildingType.farm);
        }
        else if (type=="barracks") {
            newBuilding.setType(BuildingType.barracks);
        }
        newBuilding.setHp(1);
        newBuilding.setKingdom(kingdomRepository.findByUserId(userId));
        newBuilding.setLevel(1);
        newBuilding.setStarted_at(System.currentTimeMillis() / 1000l);
        if (type=="mine") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + secondsToBuildMine);
        }
        else if (type=="farm") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + secondsToBuildMine);
        }
        else if (type=="barracks") {
            newBuilding.setFinished_at(newBuilding.getStarted_at() + secondsToBuildBarracks);
        }
        saveBuilding(newBuilding);
    }

}