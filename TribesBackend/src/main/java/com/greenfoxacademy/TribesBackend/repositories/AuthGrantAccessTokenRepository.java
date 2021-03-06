package com.greenfoxacademy.TribesBackend.repositories;

import com.greenfoxacademy.TribesBackend.models.AuthGrantAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthGrantAccessTokenRepository extends CrudRepository<AuthGrantAccessToken, Long> {
    AuthGrantAccessToken findByIdExternal(String id);
}
