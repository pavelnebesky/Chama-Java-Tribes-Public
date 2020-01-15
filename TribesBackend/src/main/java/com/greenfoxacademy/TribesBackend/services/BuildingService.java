package com.greenfoxacademy.TribesBackend.services;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.exceptions.*;
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
import java.util.stream.StreamSupport;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.*;
import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.barracks;
import static com.greenfoxacademy.TribesBackend.enums.BuildingType.townhall;
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.food;
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
        int goldToLevelUp = (building.getLevel() + 1) * GOLD_TO_LEVEL_UP_BUILDING;
        building.setLevel(building.getLevel() + 1);
        building.setFinished_at(System.currentTimeMillis() + BUILDING_TIMES.get(building.getType()));
        saveBuilding(building);
        Resource resourceToUpdate = building.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
        resourceToUpdate.setAmount(resourceToUpdate.getAmount() - goldToLevelUp);
        resourceRepository.save(resourceToUpdate);
        updateGeneration(building.getType().toString(), building.getKingdom().getId(), EXTRA_GOLD_PER_LEVEL, EXTRA_FOOD_PER_LEVEL);
        return building;
    }

    public void checkBuildingId(Long buildingId) throws IdNotFoundException {
        if (buildingId == null || !buildingRepository.findById(buildingId).isPresent()) {
            throw new IdNotFoundException(buildingId);
        }
    }

    public void checksForNewBuilding(String type, int kingdomGold, Long userId) throws InvalidBuildingTypeException, MissingParamsException, NotEnoughGoldException, TownhallAlreadyExistsException, TownhallFirstException {
        if (type == null) {
            throw new MissingParamsException(List.of("type"));
        }
        if (BuildingType.valueOf(type) == null) {
            throw new InvalidBuildingTypeException();
        }
        if (((List<Building>) getAllBuildingsByUserId(userId)).isEmpty() && !type.matches("townhall")) {
            throw new TownhallFirstException();
        }
        boolean townhallExists = ((List<Building>) (getAllBuildingsByUserId(userId))).stream().filter(b -> b.getType().equals(townhall)).findAny().isPresent();
        if (townhallExists && type.matches("townhall")) {
            throw new TownhallAlreadyExistsException();
        }
        if (kingdomGold < BUILDING_PRICE) {
            throw new NotEnoughGoldException();
        }
    }

    public void checksForUpdateBuilding(Long buildingId, Building building) throws IdNotFoundException, NotEnoughGoldException, InvalidLevelException, TownhallLevelTooLowException {
        if (!buildingRepository.findById(buildingId).isPresent()) {
            throw new IdNotFoundException(buildingId);
        }
        if (building.getLevel() <= buildingRepository.findById(buildingId).get().getLevel()) {
            throw new InvalidLevelException("building");
        }
        if ((((buildingRepository.findById(buildingId).get().getLevel()) + 1) > buildingRepository.findById(buildingId).get().getKingdom().getBuildings().stream().filter(b -> b.getType().equals(townhall)).findAny().get().getLevel()) && !(buildingRepository.findById(buildingId).get().getType() == BuildingType.valueOf("townhall"))) {
            throw new TownhallLevelTooLowException();
        }
        int kingdomsGold = buildingRepository.findById(buildingId).get().getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int goldToLevelUp = ((buildingRepository.findById(buildingId).get().getLevel()) + 1) * GOLD_TO_LEVEL_UP_BUILDING;
        if (kingdomsGold < goldToLevelUp) {
            throw new NotEnoughGoldException();
        }
    }

    public void updateGeneration(String type, long kingdomId, int addGold, int addFood) {
        var kingdomResources = resourceRepository.getAllByKingdom(kingdomRepository.findById(kingdomId).get());
        var kingdomGoldResource = kingdomResources.stream().filter(r -> r.getType().equals(gold)).findAny().get();
        var kingdomFoodResource = kingdomResources.stream().filter(r -> r.getType().equals(food)).findAny().get();
        if ((type.matches("mine")) || (type.matches("townhall"))) {
            kingdomGoldResource.setGeneration(kingdomGoldResource.getGeneration() + addGold);
        }
        if ((type.matches("farm")) || (type.matches("townhall"))) {
            kingdomFoodResource.setGeneration(kingdomFoodResource.getGeneration() + addFood);
        }
        resourceRepository.save(kingdomFoodResource);
        resourceRepository.save(kingdomGoldResource);
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
        int updatedGoldAmount = kingdomToUpdate.getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount() - BUILDING_PRICE;
        kingdomToUpdate.getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().setAmount(updatedGoldAmount);
        kingdomRepository.save(kingdomToUpdate);
        updateGeneration(type, kingdomToUpdate.getId(), GOLD_PER_MINUTE, FOOD_PER_MINUTE);
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
