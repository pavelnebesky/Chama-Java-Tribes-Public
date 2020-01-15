package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;

@Getter
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private KingdomService kingdomService;
    @Autowired
    private BuildingRepository buildingRepository;

    public ResourceType returnEnum(String type) {
        return ResourceType.valueOf(type);
    }

    public Optional<Resource> findResourceByType(ResourceType type) {
        return Optional.ofNullable(resourceRepository.findByType(type));
    }

    public List<Resource> getResources(Kingdom kingdom) {
        return resourceRepository.getAllByKingdom(kingdom);
    }

    public List<Resource> createInitialResources() {
        List<Resource> listOfInitialResources = new ArrayList<Resource>() {
            {
                add(new Resource(ResourceType.gold, 2 * BUILDING_PRICE, 0));
                add(new Resource(ResourceType.food, 0, 0));
            }
        };
        return listOfInitialResources;
    }

    public int calculateAmountOfResourceGeneratedByBuilding(Building building) {
        if (building.getType() == BuildingType.mine) {
            int differenceInTime = (int) (System.currentTimeMillis() - building.getUpdated_at()) / MILLISECONDS_PER_MINUTE;
            int AmountOfResourceToAdd = differenceInTime * (GOLD_PER_MINUTE + EXTRA_GOLD_PER_LEVEL * building.getLevel());
            building.setUpdated_at(building.getUpdated_at() + differenceInTime * MILLISECONDS_PER_MINUTE);
            buildingRepository.save(building);
            return AmountOfResourceToAdd;
        } else if (building.getType() == BuildingType.farm) {
            int differenceInTime = (int) (System.currentTimeMillis() - building.getUpdated_at()) / MILLISECONDS_PER_MINUTE;
            int AmountOfResourceToAdd = differenceInTime * (FOOD_PER_MINUTE + EXTRA_FOOD_PER_LEVEL * building.getLevel());
            building.setUpdated_at(building.getUpdated_at() + differenceInTime * MILLISECONDS_PER_MINUTE);
            buildingRepository.save(building);
            return AmountOfResourceToAdd;
        } else return 0;
    }

    public ModelMap getResourcesModelByUserId(Long userId) {
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        return new ModelMap().addAttribute("resources", this.getResources(kingdom));
    }
}
