package com.greenfoxacademy.TribesBackend.constants;

import com.greenfoxacademy.TribesBackend.enums.BuildingType;

import java.util.HashMap;
import java.util.Map;

public class BuildingConstants {

    public static final Map<BuildingType, Long> BUILDING_TIMES=new HashMap<>()
        {
            {
                put(BuildingType.mine, 10000L);
                put(BuildingType.farm, 10000L);
                put(BuildingType.townhall, 1000L);
                put(BuildingType.barracks, 10000L);
            }
        };
    public static final int GOLD_TO_LEVEL_UP_BUILDING = 250;
}
