package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
@Service
public class TroopService {
    @Autowired
    private TroopRepository troopRepository;
    @Autowired
    private UtilityService utilityService;

    public Iterable<Troop> getAllTroopsByKingdom(Kingdom kingdom){
        return troopRepository.findAllTroopsByKingdom(kingdom);
    }

    public Troop getTroopById(long trooperId){
        return troopRepository.findTrooperById(trooperId);
    }

    public ModelMap getMapOfAllTroopsByUserId(HttpServletRequest request){
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("troops", getAllTroopsByToken(request));
        return modelMap;
    }

    public Iterable<Troop> getAllTroopsByToken(HttpServletRequest request){
        return getMapOfAllTroopsByUserId(getUtilityService().)
    }
}

