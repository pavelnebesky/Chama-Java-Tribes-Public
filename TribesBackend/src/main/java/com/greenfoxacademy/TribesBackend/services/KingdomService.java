package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.repositories.kingdomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomService {

    @Autowired
    private kingdomRepository kingdomRepo;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepo.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }

    public void addNewKingdom(Kingdom kingdom){
        kingdomRepo.save(kingdom);
    }
}
