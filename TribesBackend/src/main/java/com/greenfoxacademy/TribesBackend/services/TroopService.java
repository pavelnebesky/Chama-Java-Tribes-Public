package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.constants.TroopConstants;
import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.Building;
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
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceService resourceService;

    public Troop getTroopById(Long troopId) {
        return troopRepository.findTroopById(troopId);
    }

    public void checkForFindTroopById(@PathVariable Long troopId) throws IdNotFoundException {
        Troop inMemeoryTroopId = troopRepository.findTroopById(troopId);
        if (inMemeoryTroopId == null){
            throw new IdNotFoundException(troopId);
        }
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

    public Troop createAndReturnNewTroop(Long userId) {
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

    public void checksForCreateTroop(HttpServletRequest request) throws NotEnoughGoldException, BarracksNotFoundExeption {
        Long userId = utilityService.getIdFromToken(request);
        boolean barracksExists = ((List<Building>) (buildingService.getAllBuildingsByUserId(userId))).stream().filter(b -> b.getType().equals(barracks)).findAny().isPresent();
        Kingdom homeKingdom = getKingdomRepository().findByUserId(userId);
        int kingdomsGold = homeKingdom.getResources().stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        if (!barracksExists) {
            throw new BarracksNotFoundExeption();
        }
        if (kingdomsGold < TroopConstants.TROOP_PRICE) {
            throw new NotEnoughGoldException();
        }
    }

    public Troop troopLevelUp(Troop troop, Long userId, @PathVariable Long troopId) {
        Troop troopToUpgrade = troopRepository.findTroopById(troopId);
        int goldToLevelUp = TroopConstants.TROOP_UPGRADE_PRICE;
        int newLevel = troop.getLevel();
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

    public void checksForUpgradeTroop(HttpServletRequest request, @PathVariable Long troopId, Troop troop) throws NotEnoughGoldException, InvalidLevelException, MissingParamsException, IdNotFoundException {
        if (troopRepository.findTroopById(troopId)==null){
            throw new IdNotFoundException(troopId);
        }
        Integer troopLevel = troopRepository.findTroopById(troopId).getLevel();
        Integer troopLvlFromBody = troop.getLevel();
        if (troopLvlFromBody == 0){
            troopLvlFromBody = null;
        }
        Long userId = getUserIdFromToken(request);
        List<String> missingParams = new ArrayList<String>();
        if (troopLvlFromBody == null){
            missingParams.add("level");
        }
        if (!missingParams.isEmpty()) {
            throw new MissingParamsException(missingParams);
        }
        if (resourceService.getKingdomsGoldByUserId(request) < TroopConstants.TROOP_UPGRADE_PRICE) {
            throw new NotEnoughGoldException();
        }
        if (troopLvlFromBody != troopLevel + TroopConstants.AMOUNT_OF_LEVELS_TO_ADD || troopLvlFromBody != (int) troopLvlFromBody){
            throw new InvalidLevelException("troop");
        }
    }
}