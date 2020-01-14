package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.constants.TroopConstants;
import com.greenfoxacademy.TribesBackend.exceptions.NotEnoughGoldException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.Troop;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.ResourceRepository;
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
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.food;
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
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    public Troop getTroopById(long troopId){
        return troopRepository.findTroopById(troopId);
    }

    public Iterable<Troop> getAllTroopsByUserId(Long userId) {
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

    public Troop createAndReturnNewTroop(Long userId) throws NotEnoughGoldException{
        int goldToTrain = TroopConstants.TROOP_PRICE;
        int kingdomsGold = buildingService.getKingdomRepository().findByUserId(userId).getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int barracksLevel = StreamSupport.stream(buildingService.getAllBuildingsByUserId(userId).spliterator(), false).filter(b -> b.getType().equals(barracks)).findAny().get().getLevel();
        if (kingdomsGold >= TroopConstants.TROOP_PRICE) {
            Troop newTroop = new Troop();
            newTroop.setHp(barracksLevel * TroopConstants.TROOP_BASE_HP);
            newTroop.setAttack(1);
            newTroop.setDefence(1);
            newTroop.setStarted_at(System.currentTimeMillis());
            newTroop.setFinished_at(newTroop.getStarted_at() + TroopConstants.TROOP_TRAINING_TIME);
            newTroop.setKingdom(kingdomService.getKingdomByUserId(userId));
            saveTroop(newTroop);
            Kingdom kingdomToUpdate = kingdomRepository.findByUserId(userId);
            List<Troop> kingdomTroops = kingdomToUpdate.getTroops();
            Resource resourceToUpdate = newTroop.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
            resourceToUpdate.setAmount(resourceToUpdate.getAmount() - goldToTrain);
            resourceRepository.save(resourceToUpdate);
            kingdomTroops.add(newTroop);
            kingdomRepository.save(kingdomToUpdate);
            return newTroop;
        }else throw new NotEnoughGoldException();
    }

    public Troop troopLevelUp(Troop troop, Long userId) throws NotEnoughGoldException{
        int kingdomsGold = troop.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        int troopLevel = StreamSupport.stream(troopRepository.findAllTroopsByKingdomUserId(userId).spliterator(), false).findAny().get().getLevel();
        int goldToLevelUp = TroopConstants.TROOP_UPGRADE_PRICE;
        if (goldToLevelUp <= kingdomsGold) {
            int newLevel = troopLevel + TroopConstants.AMOUNT_OF_LEVELS_TO_ADD;
            troop.setLevel(newLevel);
            troop.setAttack(troop.getAttack() + TroopConstants.AMOUNT_OF_STATS_TO_ADD);
            troop.setDefence(getTroopById(troop.getId()).getDefence() + TroopConstants.AMOUNT_OF_STATS_TO_ADD);
            saveTroop(troop);
            Resource resourceToUpdate = troop.getKingdom().getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
            resourceToUpdate.setAmount(resourceToUpdate.getAmount() - goldToLevelUp);
            resourceRepository.save(resourceToUpdate);
            return troop;
        }else throw new NotEnoughGoldException();
    }

    public void troopConsumtion(Long id){
        int foodConsumedByAllTroops = 0;
        List<Troop> troops = getKingdomService().getKingdomByUserId(id).getTroops();
        for (Troop troop: troops) {
            foodConsumedByAllTroops = foodConsumedByAllTroops + TroopConstants.FOOD_CONSUMED_BY_ONE;
        }
        int kingdomsFood = resourceRepository.findByType(food).getAmount();
        Kingdom kingdomToUdate = getKingdomService().getKingdomByUserId(id);
        if (foodConsumedByAllTroops <= kingdomsFood){
            Resource resourceToUpdate = getKingdomService().getKingdomByUserId(id).getResources().stream().filter(r -> r.getType().equals(food)).findAny().get();
            resourceToUpdate.setAmount(resourceToUpdate.getAmount() - foodConsumedByAllTroops);
            resourceRepository.save(resourceToUpdate);
        }
    }
}