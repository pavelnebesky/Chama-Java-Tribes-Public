package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.repositories.kingdomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class KingdomService {

    @Autowired
    private kingdomRepository kingdomRepo;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepo.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }
}
