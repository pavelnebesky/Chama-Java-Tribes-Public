package com.greenfoxacademy.TribesBackend.repositories;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface kingdomRepository extends CrudRepository<Kingdom, Long> {
    Kingdom findByName(String name);
}
