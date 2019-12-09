package com.greenfoxacademy.TribesBackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping(value="/")
    public String index() {
        return "index";
    }

}
