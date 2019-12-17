package com.greenfoxacademy.TribesBackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {

    @GetMapping("/kingdom")
    public String getKingdom(){
        return "test";
    }
}
