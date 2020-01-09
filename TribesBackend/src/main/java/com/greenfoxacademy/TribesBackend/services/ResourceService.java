package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private KingdomService kingdomService;

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

    public double calculateAmountOfGold(Long userId) {
        
    }

    public ModelMap getResourcesModelByUserId(Long userId) {
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        return new ModelMap().addAttribute("resources", this.getResources(kingdom));
    }
}
