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
    public ResponseEntity getResourceType(@PathVariable String ResourceType) {
        ResourceType type = resourceService.returnEnum(ResourceType);
        Optional<Resource> maybeResource = resourceService.findResourceByType(type);
        try {
            resourceService.checkResourceTypeIfItExists(ResourceType);
        } catch (ParameterNotFoundException e) {
            return resourceService.getUtilityService().handleResponseWithException(e);
        }
        return ResponseEntity.ok(maybeResource);
    }
}

