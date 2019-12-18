package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    private KingdomRepository kingdomRepository;

    public boolean isResourceNameValid(String name) {
        return resourceRepository.findByName(name) != null;
    }

    public List<Resource> getResources(Kingdom kingdom){
        return kingdomRepository.getKingdomsResources(kingdom);
    }
}
