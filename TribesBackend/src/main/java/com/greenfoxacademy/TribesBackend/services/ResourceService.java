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
import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public resourceType returnEnum(String type) {
        return resourceType.valueOf(type);
    }

    public Resource findResourceByType(resourceType type) {
        return resourceRepository.findByType(type);
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


}
