package com.greenfoxacademy.TribesBackend.repositories;
import com.greenfoxacademy.TribesBackend.models.Troop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TroopRepository extends CrudRepository<Troop, Long> {
    Iterable<Troop> findAllTroopsByKingdomUserId(Long id);
    Troop findTrooperById(long id);
}
