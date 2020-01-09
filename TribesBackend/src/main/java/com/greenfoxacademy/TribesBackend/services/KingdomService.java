package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Getter
@Service
public class KingdomService {

    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private UtilityService utilityService;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepository.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }

    public Kingdom getKingdomByUserId(Long userId) {
        return kingdomRepository.findByUserId(userId);
    }

    public void updateKingdom(Kingdom kingdomToUpdate, ModelMap kingdomDataToUpdate) {
        if (kingdomDataToUpdate.containsAttribute("name")){
            kingdomToUpdate.setName((String)kingdomDataToUpdate.getAttribute("name"));
        }
        if (kingdomDataToUpdate.containsAttribute("locationX")) {
            kingdomToUpdate.getLocation().setX((int) kingdomDataToUpdate.getAttribute("locationX"));
        }
        if (kingdomDataToUpdate.containsAttribute("locationY")) {
            kingdomToUpdate.getLocation().setY((int) kingdomDataToUpdate.getAttribute("locationY"));
        }
        kingdomRepository.save(kingdomToUpdate);
    }
}
