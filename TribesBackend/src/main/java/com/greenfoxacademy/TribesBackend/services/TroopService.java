package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Getter
@Setter
@Service
public class TroopService {

    @Autowired
    private TroopRepository troopRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    public Troop createTrooperToKingdom (Kingdom kingdom){
        Troop newTrooper = new Troop(kingdom);
        newTrooper.setKingdom(kingdomRepository.)
    }
}
