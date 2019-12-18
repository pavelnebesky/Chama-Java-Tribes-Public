package com.greenfoxacademy.TribesBackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @GetMapping("/kingdom/resources")
    public String getResource() {
        return "something";
    }

    @GetMapping("/kingdom/resources/{resourceType}")
    public String getResourceType() {
        return "something";
    }
}
