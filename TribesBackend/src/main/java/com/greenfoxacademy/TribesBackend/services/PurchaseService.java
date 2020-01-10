package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.constants.TroopConstants;
import com.greenfoxacademy.TribesBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

import static com.greenfoxacademy.TribesBackend.enums.BuildingType.barracks;
import static com.greenfoxacademy.TribesBackend.enums.ResourceType.gold;

@Service
public class PurchaseService {

    @Autowired
    BuildingService buildingService;

    public boolean userHasEnoughGoldForTroop(long userId) {
        int kingdomsGold = buildingService.getKingdomRepository().findByUserId(userId).getResources()
                .stream().filter(r -> r.getType().equals(gold)).findAny().get().getAmount();
        return kingdomsGold >= TroopConstants.TROOP_PRICE;
    }
}
