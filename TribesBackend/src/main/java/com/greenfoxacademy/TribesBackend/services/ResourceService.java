package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    private KingdomRepository kingdomRepository;

    public boolean isResourceNameValid(String name) {
        return resourceRepository.findByName(name) != null;
    }

    public List<Resource> getResources(){
        return getResources();
    }
}
