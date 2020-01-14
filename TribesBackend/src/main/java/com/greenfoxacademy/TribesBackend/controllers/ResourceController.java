package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.exceptions.ParameterNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.services.ResourceService;
import com.greenfoxacademy.TribesBackend.services.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UtilityService utilityService;

    @GetMapping("/kingdom/resources")
    public ResponseEntity getResource(HttpServletRequest request) {
        Long userId = resourceService.getUtilityService().getIdFromToken(request);
        return ResponseEntity.ok(resourceService.getResourcesModelByUserId(userId));
    }

    @GetMapping("/kingdom/resources/{resourceType}")
    public ResponseEntity getResourceType(@PathVariable String resourceType, HttpServletRequest request) {
        try {
            resourceService.checkResourceTypeIfItExists(resourceType);
        } catch (IllegalArgumentException e) {
            return resourceService.getUtilityService().handleResponseWithException(new ParameterNotFoundException(resourceType));
        }
        Long id = resourceService.getUtilityService().getIdFromToken(request);
        return ResponseEntity.ok(resourceService.getResourceTypeModelByUserId(id, resourceType));
    }
}

