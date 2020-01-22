package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.BUILDING_PRICE;
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.gold;

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

    public Resource findResourceByType(ResourceType type) {
        return resourceRepository.findByType(type);
    }

    public Resource findResourceByTypeAndUserId(ResourceType type, Long userId) {
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        List<Resource> resources = resourceRepository.getAllByKingdom(kingdom);
        for (Resource resource : resources) {
            if (resource.getType() == type) {
                return resource;
            }
        }
        return null;
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

    public ModelMap getResourcesModelByUserId(Long userId) {
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        return new ModelMap().addAttribute("resources", this.getResources(kingdom));
    }

    public void checkResourceTypeIfItExists(String resourceType) throws IllegalArgumentException {
        ResourceType.valueOf(resourceType);
    }

    public int getKingdomsGoldByUserId(HttpServletRequest request){
        Long userId = utilityService.getIdFromToken(request);
        Kingdom kingdom = getKingdomService().getKingdomByUserId(userId);
        int kingdomsGold = kingdom.getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        return kingdomsGold;
    }
}
