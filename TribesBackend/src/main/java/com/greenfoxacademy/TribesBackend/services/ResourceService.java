package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.resourceType;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
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
    private AuthenticationService authenticationService;
    @Autowired
    private KingdomService kingdomService;

    public resourceType returnEnum(String type) {
        return resourceType.valueOf(type);
    }

    public Optional<Resource> findResourceByType(resourceType type) {
        return Optional.ofNullable(resourceRepository.findByType(type));
    }

    public List<Resource> getResources(Kingdom kingdom) {
        return resourceRepository.getAllByKingdom(kingdom);
    }

    public List<Resource> createInitialResources() {
        List<Resource> listOfInitialResources = new ArrayList<Resource>(){
            {
            add(new Resource(resourceType.gold, 2 * BUILDING_PRICE, 0));
            add(new Resource(resourceType.food, 0, 0));
            }
        };
        return listOfInitialResources;
    }

    public ModelMap getResourcesModelByUserId(Long userId) {
        Kingdom kingdom = kingdomService.getKingdomByUserId(userId);
        return new ModelMap().addAttribute("resources", this.getResources(kingdom));
    }
}
