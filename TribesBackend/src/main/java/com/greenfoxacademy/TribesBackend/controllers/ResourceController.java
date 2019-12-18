package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.services.ResourceService;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/kingdom/resources")
    public String getResource(Kingdom kingdom) {
        resourceService.getResources(kingdom);
        return "something";
    }

    @GetMapping("/kingdom/resources/{resourceType}")
    public String getResourceType() {
        return "something";
    }
}
