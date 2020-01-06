package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TroopService {

    @Autowired
    TroopRepository troopRepository;

   // public Troop troopLvlUp(long id, Kingdom kingdom){}
}
