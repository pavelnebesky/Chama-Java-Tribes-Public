package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class KingdomService {

    @Autowired
    private KingdomRepository kingdomRepository;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepository.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }
}
