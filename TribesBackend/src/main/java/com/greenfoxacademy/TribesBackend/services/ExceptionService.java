package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.FrontendException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExceptionService {

    public ResponseEntity handleResponseWithException(FrontendException e){
        return ResponseEntity.status(e.getSc()).body(e.getMessage());
    }
}
