package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KingdomService {

    @Autowired
    private KingdomRepository kingdomRepo;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepo.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }

    public Kingdom getKingdomByUserId(Long userId) {
        return kingdomRepo.findByUserId(userId);
    }
}
