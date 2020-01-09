package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.NotEnoughGoldException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.repositories.TroopRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.barracks;
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.gold;

@Getter
@Setter
@Service
public class TroopService {
    
    @Autowired
    private TroopRepository troopRepository;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private KingdomService kingdomService;

    static final long TRAINING_TIME = 60000;

    public Troop getTrooperById(long trooperId){
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

    public Troop saveTroop(Troop troop){
        troopRepository.save(troop);
        return troop;
    }

    public Troop createAndReturnNewTroop(long userId) throws NotEnoughGoldException{
        //int barraksLevel = buildingService.getAllBuildingsByUserId(userId).toList().stream().filter(b -> b.getType().equals(barracks)).findAny().get().getLevel();
        int kingdomsGold = buildingService.getKingdomRepository().findByUserId(userId).getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int barracksLevel = StreamSupport.stream(buildingService.getAllBuildingsByUserId(userId).spliterator(), false).filter(b -> b.getType().equals(barracks)).findAny().get().getLevel();
        if (kingdomsGold >= 10) {
            Troop newTroop = new Troop();
            newTroop.setHp(barracksLevel * 10);
            newTroop.setAttack(1);
            newTroop.setDefence(1);
            newTroop.setStarted_at(System.currentTimeMillis());
            newTroop.setFinished_at(newTroop.getStarted_at() + TRAINING_TIME);
            newTroop.setKingdom(kingdomService.getKingdomByUserId(userId));
            saveTroop(newTroop);
            Kingdom kingdomToUpdate = buildingService.getKingdomRepository().findByUserId(userId);
            List<Troop> kingdomTroops = troopRepository.findAllTroopsByKingdomUserId(userId);
            kingdomTroops.add(newTroop);
            return newTroop;
        }else throw new NotEnoughGoldException();

    }
}

