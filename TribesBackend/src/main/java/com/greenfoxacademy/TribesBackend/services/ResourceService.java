package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.resourceType;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Resource> getResources(Kingdom kingdom){
        return resourceRepository.getAllByKingdom(kingdom);
    }
}
