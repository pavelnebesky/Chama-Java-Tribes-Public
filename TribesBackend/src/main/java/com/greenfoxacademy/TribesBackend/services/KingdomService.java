package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class KingdomService {

    @Autowired
    private KingdomRepository kingdomRepository;

    public boolean isKingdomNameOkToSave(String name, Kingdom kingdom) {
        if (!name.matches(kingdomRepository.findByName(kingdom.getName())) && name.length() > 6 && name.matches("[a-zA-Z]+"));
        {
            return true;
        }
    }

}
