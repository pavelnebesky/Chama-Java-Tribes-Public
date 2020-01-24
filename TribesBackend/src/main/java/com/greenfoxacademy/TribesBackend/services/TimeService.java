package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.*;
import com.greenfoxacademy.TribesBackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.BUILDING_TIMES;
import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.*;
import static com.greenfoxacademy.TribesBackend.constants.TroopConstants.FOOD_CONSUMED_BY_ONE;
import static com.greenfoxacademy.TribesBackend.constants.TroopConstants.TROOP_TRAINING_TIME;

@Service
public class TimeService {


    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private TroopRepository troopRepository;

    public double getMinutesBetweenTimeStamps(Long firstTimeStamp, Long secondTimeStamp) {
        return Math.abs((firstTimeStamp - secondTimeStamp) / 60000.0);
    }

    public Long calculateUpgradingBuildingTime(int level, String type) {
        return level * BUILDING_TIMES.get(BuildingType.valueOf(type));
    }

    public Long calculateUpgradingTroopTime(int level) {
        return level * TROOP_TRAINING_TIME;
    }

    public int differenceInTimeInMinutes(Long firstTimeStamp, Long secondTimeStamp) {
        if (secondTimeStamp - firstTimeStamp < 0) {
            return 0;
        }
        return (int) ((secondTimeStamp - firstTimeStamp) / MILLISECONDS_PER_MINUTE);
    }

    public int calculateAmountOfResourceGenerated(int resourcePerMinute, int extraResourcePerLevel, Building building) {
        return differenceInTimeInMinutes(building.getUpdated_at(), System.currentTimeMillis()) * (resourcePerMinute + extraResourcePerLevel * (building.getLevel() - 1));
    }

    public void calculateTownHallGeneration(Resource goldResource, Resource foodResource, Building townhall) {
        goldResource.setAmount(goldResource.getAmount() + calculateAmountOfResourceGenerated(GOLD_PER_MINUTE, EXTRA_GOLD_PER_LEVEL, townhall));
        foodResource.setAmount(foodResource.getAmount() + calculateAmountOfResourceGenerated(FOOD_PER_MINUTE, EXTRA_FOOD_PER_LEVEL, townhall));
        townhall.setUpdated_at(townhall.getUpdated_at() + differenceInTimeInMinutes(townhall.getUpdated_at(), System.currentTimeMillis()) * MILLISECONDS_PER_MINUTE);
        buildingRepository.save(townhall);
    }

    public int calculateAmountOfResourceGeneratedByBuilding(Building building) {
        if (building.getUpdated_at() < building.getFinished_at()) {
            building.setUpdated_at(building.getFinished_at());
        }
        int amount = 0;
        if (building.getType() == BuildingType.mine) {
            amount = calculateAmountOfResourceGenerated(GOLD_PER_MINUTE, EXTRA_GOLD_PER_LEVEL, building);
        } else if (building.getType() == BuildingType.farm) {
            amount = calculateAmountOfResourceGenerated(FOOD_PER_MINUTE, EXTRA_FOOD_PER_LEVEL, building);
        }
        building.setUpdated_at(building.getUpdated_at() + differenceInTimeInMinutes(building.getUpdated_at(), System.currentTimeMillis()) * MILLISECONDS_PER_MINUTE);
        buildingRepository.save(building);
        return amount;
    }

    public int calculateAmountOfResourceGeneratedByBuildings(List<Building> buildings) {
        int sum = 0;
        for (int i = 0; i < buildings.size(); i++) {
            sum += calculateAmountOfResourceGeneratedByBuilding(buildings.get(i));
        }
        return sum;
    }

    public int amountBySpecificType(BuildingType type, List<Building> buildings) {
        List<Building> specificBuildings = buildings.stream().filter(b -> b.getType() == type).collect(Collectors.toList());
        return calculateAmountOfResourceGeneratedByBuildings(specificBuildings);
    }

    public void updateAllUserData(Long userId) {
        List<Building> buildings = (List<Building>) buildingRepository.findAllByKingdomUserId(userId);
        Building townhall = buildings.stream().filter(b -> b.getType() == BuildingType.townhall).findFirst().orElse(null);
        Long troopUpdated_at = townhall.getUpdated_at();
        int generatedGold = amountBySpecificType(BuildingType.mine, buildings);
        int generatedFood = amountBySpecificType(BuildingType.farm, buildings);
        List<Resource> resources = resourceRepository.findAllByKingdomUserId(userId);
        Resource goldResource = resources.stream().filter(r -> r.getType() == ResourceType.gold).findAny().get();
        Resource foodResource = resources.stream().filter(r -> r.getType() == ResourceType.food).findAny().get();
        goldResource.setAmount(goldResource.getAmount() + generatedGold);
        Optional<Building> optTownhall = buildings.stream().filter(b -> b.getType() == BuildingType.townhall).findAny();
        foodResource.setAmount(foodResource.getAmount() + generatedFood);
        if (optTownhall.isPresent()) {
            calculateTownHallGeneration(goldResource, foodResource, optTownhall.get());
        }
        foodResource.setAmount(updateFood(differenceInTimeInMinutes(troopUpdated_at, System.currentTimeMillis()), userId, foodResource.getAmount(), foodResource.getGeneration()));
        resourceRepository.saveAll(List.of(goldResource, foodResource));
    }

    public int updateFood(int minutesPassed, Long userId, int totalFood, int foodGeneration) {
        List<Troop> troops = troopRepository.findAllTroopsByKingdomUserId(userId);
        int consumedFood = calculateConsumedFood(troops, minutesPassed);
        totalFood -= consumedFood;
        if (totalFood < 0) {
            for (int i = 0; i < troops.size(); i++) {
                if (totalFood == 0) {
                    break;
                }
                if (troops.size() > foodGeneration) {
                    troops.get(i).setHp(troops.get(i).getHp() - 1);
                    totalFood++;
                    if (troops.get(i).getHp() < 1) {
                        Long id = troops.get(i).getId();
                        troopRepository.deleteById(id);
                        troops.remove(i);
                        i--;
                    } else {
                        troopRepository.save(troops.get(i));
                    }
                }
            }
            totalFood = 0;
        }
        return totalFood;
    }

    public int calculateConsumedFood(List<Troop> troops, int minutesPassed) {
        int consumedFood = 0;
        for (Troop troop : troops) {
            if (troop.getFinished_at() < System.currentTimeMillis()) {
                consumedFood += FOOD_CONSUMED_BY_ONE * minutesPassed;
            } else {
                consumedFood += FOOD_CONSUMED_BY_ONE * differenceInTimeInMinutes(troop.getFinished_at(), System.currentTimeMillis());
            }
        }
        return consumedFood;
    }
}
