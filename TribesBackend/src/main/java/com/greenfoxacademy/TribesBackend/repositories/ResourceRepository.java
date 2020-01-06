package com.greenfoxacademy.TribesBackend.repositories;
import com.greenfoxacademy.TribesBackend.enums.resourceType;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
    Resource findByType(resourceType type);
    List<Resource> getAllByKingdom(Kingdom kingdom);
}