package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import com.greenfoxacademy.TribesBackend.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private KingdomRepository kingdomRepository;

    @GetMapping("/kingdom/resources")
    public String getResource(String name) {
        Kingdom kingdom = kingdomRepository.findByName(name);
        resourceService.getResources(kingdom);
        return "something";
    }

    @GetMapping("/kingdom/resources/{resourceType}")
    public String getResourceType(@PathVariable String resourceType) {
        return "something";
    }
}
