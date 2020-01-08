package com.greenfoxacademy.TribesBackend.services;

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

    public Troop getTroopById(long trooperId){
        return troopRepository.findTrooperById(trooperId);
    }

    public Iterable<Troop> getAllTroopsByUserId(long userId) {
        return troopRepository.findAllTroopsByKingdomUserId(userId);
    }

    public ModelMap getModelMapOfAllTroopsByUserId(HttpServletRequest request){
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("troops", getAllTroopsByToken(request));
        return modelMap;
    }

    public Iterable<Troop> getAllTroopsByToken(HttpServletRequest request){
        return getAllTroopsByUserId(getUtilityService().getIdFromToken(request));
    }
}

