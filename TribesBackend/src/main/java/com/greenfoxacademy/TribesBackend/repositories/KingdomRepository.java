package com.greenfoxacademy.TribesBackend.repositories;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.Resource;
import com.greenfoxacademy.TribesBackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingdomRepository extends CrudRepository<Kingdom, Long> {
    Kingdom findByName(String name);
    Kingdom findByUser(User user);
    Kingdom findByUserId(Long userId);
}
