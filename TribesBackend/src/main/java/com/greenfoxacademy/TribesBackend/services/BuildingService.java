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
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.*;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.townhall;
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.gold;

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
    private UtilityService utilityService;

    public Building saveBuilding(Building building) {
        buildingRepository.save(building);
        return building;
    }

    public Iterable<Building> getAllBuildingsByUserId(long userId) {
        return buildingRepository.findAllByKingdomUserId(userId);
    }

    public Building getBuildingById(long buildingId) {
        return buildingRepository.findById(buildingId).get();
    }

    public ModelMap getMapOfAllBuildingsByToken(HttpServletRequest request) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("buildings", getBuildingsByToken(request));
        return modelMap;
    }

    public Iterable<Building> getBuildingsByToken(HttpServletRequest request) {
        return getAllBuildingsByUserId(getUtilityService().getIdFromToken(request));
    }

    public Building buildingLevelUp(Building building, int newLevel) {
        int kingdomsGold = building.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int townhallLevel = building.getKingdom().getBuildings().stream().filter(b -> b.getType().equals(townhall)).findAny().get().getLevel();
        int goldToLevelUp = (newLevel - building.getLevel()) * GOLD_TO_LEVEL_UP_BUILDING;
        if (newLevel <= townhallLevel && goldToLevelUp <= kingdomsGold) {
            building.setLevel(newLevel);
            saveBuilding(building);
            Resource resourceToUpdate = building.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
            resourceToUpdate.setAmount(resourceToUpdate.getAmount() - goldToLevelUp);
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
        newBuilding.setFinished_at(newBuilding.getStarted_at() + BUILDING_TIMES.get(BuildingType.valueOf(type)));
        saveBuilding(newBuilding);
        Kingdom kingdomToUpdate = kingdomRepository.findByUserId(userId);
        List<Building> kingdomsBuildings = kingdomToUpdate.getBuildings();
        kingdomsBuildings.add(newBuilding);
        kingdomToUpdate.setBuildings(kingdomsBuildings);
        kingdomRepository.save(kingdomToUpdate);
        return newBuilding;
    }

    public ModelMap getLeaderboard() {
        Map<String, Integer> leaderboardDictionary = new HashMap<String, Integer>();
        for (Kingdom kingdom : kingdomRepository.findAll()
        ) {
            leaderboardDictionary.put(kingdom.getName(), kingdom.getBuildings().size());
        }
        Map<String, Integer> sortedLeaderboardDictionary = leaderboardDictionary.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        ArrayList<ModelMap> leaderboard = new ArrayList<ModelMap>();
        for (Map.Entry<String, Integer> entry : sortedLeaderboardDictionary.entrySet()) {
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("kingdomname", entry.getKey());
            modelMap.addAttribute("buildings", entry.getValue());
            leaderboard.add(modelMap);
        }
        return new ModelMap().addAttribute("leaderboard", leaderboard);
    }
}
