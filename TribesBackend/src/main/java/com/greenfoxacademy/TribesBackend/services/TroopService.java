package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopService {

    @Autowired
    TroopRepository troopRepository;
}
