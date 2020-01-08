package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import org.springframework.stereotype.Service;

import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.BUILDING_TIMES;
import static com.greenfoxacademy.TribesBackend.constants.TroopConstants.TROOP_TRAINING_TIME;

@Service
public class TimeService {

    public double getMinutesBetweenTimeStamps(Long firstTimeStamp, Long secondTimeStamp) {
        return Math.abs((firstTimeStamp - secondTimeStamp) / 60000.0);
    }

    public Long calculateUpgradingBuildingTime(int level, String type) {
        return level * BUILDING_TIMES.get(BuildingType.valueOf(type));
    }

    public Long calculateUpgradingTroopTime(int level) {
        return level * TROOP_TRAINING_TIME;
    }
}
