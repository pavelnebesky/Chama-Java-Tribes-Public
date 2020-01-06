package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;

@Getter
@Service
public class KingdomService {

    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public boolean isKingdomNameValid(String name) {
        return kingdomRepository.findByName(name) == null
                && name.length() > 6
                && name.matches("[a-zA-Z]+");
    }

    public Kingdom getKingdomByUserId(Long userId) {
        return kingdomRepository.findByUserId(userId);
    }

    public void updateKingdom(Kingdom kingdomToUpdate, ModelMap updatedVars) {
        if (updatedVars.containsAttribute("name")){
            kingdomToUpdate.setName((String)updatedVars.getAttribute("name"));
        }
        if (updatedVars.containsAttribute("locationX")) {
            kingdomToUpdate.getLocation().setX((int) updatedVars.getAttribute("locationX"));
        }
        if (updatedVars.containsAttribute("locationY")) {
            kingdomToUpdate.getLocation().setY((int) updatedVars.getAttribute("locationY"));
        }
        kingdomRepository.save(kingdomToUpdate);
    }
}
