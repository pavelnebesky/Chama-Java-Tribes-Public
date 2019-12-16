package com.greenfoxacademy.TribesBackend.repositories;
import com.greenfoxacademy.TribesBackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends CrudRepository<User, Long> {
}
