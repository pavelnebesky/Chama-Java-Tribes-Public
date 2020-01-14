package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.Building;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.BuildingRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.BUILDING_TIMES;
import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;
import static com.greenfoxacademy.TribesBackend.constants.TroopConstants.TROOP_TRAINING_TIME;

@Service
public class TimeService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KingdomRepository kingdomRepository;

    public double getMinutesBetweenTimeStamps(Long firstTimeStamp, Long secondTimeStamp) {
        return Math.abs((firstTimeStamp - secondTimeStamp) / 60000.0);
    }

    public Long calculateUpgradingBuildingTime(int level, String type) {
        return level * BUILDING_TIMES.get(BuildingType.valueOf(type));
    }

    public Long calculateUpgradingTroopTime(int level) {
        return level * TROOP_TRAINING_TIME;
    }

    public int calculateAmountOfResourceGeneratedByBuilding(Building building) {
        if (building.getType() == BuildingType.mine) {
            int differenceInTime = (int) (System.currentTimeMillis() - building.getUpdated_at()) / MILLISECONDS_PER_MINUTE;
            int AmountOfResourceToAdd = differenceInTime * (GOLD_PER_MINUTE + EXTRA_GOLD_PER_LEVEL * building.getLevel());
            building.setUpdated_at(building.getUpdated_at() + differenceInTime * MILLISECONDS_PER_MINUTE);
            buildingRepository.save(building);
            return AmountOfResourceToAdd;
        } else if (building.getType() == BuildingType.farm) {
            int differenceInTime = (int) (System.currentTimeMillis() - building.getUpdated_at()) / MILLISECONDS_PER_MINUTE;
            int AmountOfResourceToAdd = differenceInTime * (FOOD_PER_MINUTE + EXTRA_FOOD_PER_LEVEL * building.getLevel());
            building.setUpdated_at(building.getUpdated_at() + differenceInTime * MILLISECONDS_PER_MINUTE);
            buildingRepository.save(building);
            return AmountOfResourceToAdd;
        } else return 0;
    }

    public int calculateAmountOfResourceGeneratedByBuildings(List<Building> buildings){
        int sum=0;
        for (int i=0;i<buildings.size();i++) {
            sum+=calculateAmountOfResourceGeneratedByBuilding(buildings.get(i));
        }
        return sum;
    }

    public void updateAllUserData(Long userId){
        User user=userRepository.findById(userId).get();
        Kingdom kingdom=user.getKingdom();
        List<Building> mines=kingdom.getBuildings().stream().collect(Collectors.toList());
        //filter(b->b.getType()==BuildingType.mine).collect(Collectors.toList());
        int generatedGold=calculateAmountOfResourceGeneratedByBuildings(mines);
        List<Resource> resources=kingdom.getResources();
        int listIndex=resources.indexOf(resources.stream().filter(r->r.getType()== ResourceType.gold).findAny().get());
        resources.get(listIndex).setAmount(resources.get(listIndex).getAmount()+generatedGold);
        kingdom.setResources(resources);
        kingdomRepository.save(kingdom);
    }
}
