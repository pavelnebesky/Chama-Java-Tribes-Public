package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.constants.TroopConstants;
import com.greenfoxacademy.TribesBackend.exceptions.IdNotFoundException;
import com.greenfoxacademy.TribesBackend.exceptions.InvalidLevelException;
import com.greenfoxacademy.TribesBackend.exceptions.MissingParamsException;
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
import java.util.ArrayList;
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

    public Troop getTroopById(Long troopId) {
        return troopRepository.findTroopById(troopId);
    }

    public Iterable<Troop> getAllTroopsByUserId(Long userId) {
        return troopRepository.findAllTroopsByKingdomUserId(userId);
    }

    public ModelMap getModelMapOfAllTroopsByUserId(HttpServletRequest request) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("troops", getAllTroopsByToken(request));
        return modelMap;
    }

    public Long getUserIdFromToken(HttpServletRequest request){
        return  getUtilityService().getIdFromToken(request).longValue();
    }

    public Iterable<Troop> getAllTroopsByToken(HttpServletRequest request) {
        return getAllTroopsByUserId(getUtilityService().getIdFromToken(request));
    }

    public Troop saveTroop(Troop troop) {
        troopRepository.save(troop);
        return troop;
    }

    public Troop createAndReturnNewTroop(Long userId) throws NotEnoughGoldException {
        int goldToTrain = TroopConstants.TROOP_PRICE;
        int barracksLevel = StreamSupport.stream(buildingService.getAllBuildingsByUserId(userId).spliterator(), false).filter(b -> b.getType().equals(barracks)).findAny().get().getLevel();
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
    }

    public Troop troopLevelUp(Troop troop, Long userId) {

        Troop troopToUpgrade = StreamSupport.stream(troopRepository.findAllTroopsByKingdomUserId(userId).spliterator(), false).findAny().get();
        int troopLevel = troopToUpgrade.getLevel();
        int goldToLevelUp = TroopConstants.TROOP_UPGRADE_PRICE;
        int newLevel = troopLevel;
        Kingdom kingdomToUpdate = kingdomRepository.findByUserId(userId);
        troopToUpgrade.setLevel(newLevel);
        troopToUpgrade.setAttack(troopToUpgrade.getAttack() + TroopConstants.AMOUNT_OF_STATS_TO_ADD);
        troopToUpgrade.setDefence(getTroopById(troopToUpgrade.getId()).getDefence() + TroopConstants.AMOUNT_OF_STATS_TO_ADD);
        troopToUpgrade.setKingdom(kingdomToUpdate);
        troopToUpgrade.setStarted_at(System.currentTimeMillis());
        troopToUpgrade.setFinished_at(troopToUpgrade.getStarted_at() + TroopConstants.TROOP_TRAINING_TIME);
        saveTroop(troopToUpgrade);
        Resource resourceToUpdate = kingdomRepository.findByUserId(userId).getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get();
        resourceToUpdate.setAmount(resourceToUpdate.getAmount() - goldToLevelUp);
        resourceRepository.save(resourceToUpdate);
        return troopToUpgrade;
    }

    public void checksForUpgradeTroop(HttpServletRequest request, Long troopId, Troop troop) throws NotEnoughGoldException, InvalidLevelException, MissingParamsException, IdNotFoundException {
        Integer troopLevel = troopRepository.findTroopById(troopId).getLevel();
        Integer troopLvlFromBody = troop.getLevel();
        if (troopLvlFromBody == 0){
            troopLvlFromBody = null;
        }
        Long userId = getUserIdFromToken(request);
        Kingdom homeKingdom = getKingdomRepository().findByUserId(userId);
        int kingdomsGold = homeKingdom.getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount()
        List<String> missingParams = new ArrayList<String>();
        if (troopLvlFromBody == null){
            missingParams.add("level");
        }
        if (!missingParams.isEmpty()) {
            throw new MissingParamsException(missingParams);
        }
        if (kingdomsGold < TroopConstants.TROOP_UPGRADE_PRICE) {
            throw new NotEnoughGoldException();
        }
        if (troopLvlFromBody != troopLevel + TroopConstants.AMOUNT_OF_LEVELS_TO_ADD || troopLvlFromBody != (int) troopLvlFromBody){
            throw new InvalidLevelException("troop");
        }
        Long inMemeoryTroopId = troopRepository.findTroopById(troopId).getId();
        if (troopId != inMemeoryTroopId){
            throw new IdNotFoundException(troopId);
        }
    }

    public void troopConsumption(Long id){
        int foodConsumedByAllTroops = 0;
        List<Troop> troops = getKingdomService().getKingdomByUserId(id).getTroops();
        for (Troop troop: troops) {
            foodConsumedByAllTroops += TroopConstants.FOOD_CONSUMED_BY_ONE;
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