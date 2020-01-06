package com.greenfoxacademy.TribesBackend.controllers;

import com.greenfoxacademy.TribesBackend.enums.resourceType;
import com.greenfoxacademy.TribesBackend.exceptions.ParameterNotFoundException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.services.ExceptionService;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import com.greenfoxacademy.TribesBackend.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ExceptionService exceptionService;

    @GetMapping("/kingdom/resources")
    public ResponseEntity getResource(HttpServletRequest request) {
        Long userId = resourceService.getAuthenticationService().getIdFromToken(request);
        return ResponseEntity.ok(resourceService.getResourcesModelByUserId(userId));
    }

    @GetMapping("/kingdom/resources/{resourceType}")
    public ResponseEntity getResourceType(@PathVariable String resourceType) {
        resourceType type = resourceService.returnEnum(resourceType);
        Resource maybeResource = resourceService.findResourceByType(type);
        if (maybeResource != null) return ResponseEntity.ok(maybeResource);
        else return exceptionService.handleResponseWithException(new ParameterNotFoundException(resourceType));
    }
}

