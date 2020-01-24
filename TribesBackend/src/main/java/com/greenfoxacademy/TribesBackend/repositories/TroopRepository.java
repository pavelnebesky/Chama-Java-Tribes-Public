package com.greenfoxacademy.TribesBackend.repositories;

import com.greenfoxacademy.TribesBackend.models.Troop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TroopRepository extends CrudRepository<Troop, Long> {
    List<Troop> findAllTroopsByKingdomUserId(Long id);
    Troop findTroopById(Long id);
    Troop getByIdIsNotNull();
    void deleteById(Long troopId);
}
