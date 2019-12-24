package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class ExceptionService {

    public ResponseEntity handleResponseWithException(FrontendException e) {
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("status", "error");
        modelMap.addAttribute("error", e.getMessage());
        return ResponseEntity.status(e.getSc()).body(modelMap);
    }
}
