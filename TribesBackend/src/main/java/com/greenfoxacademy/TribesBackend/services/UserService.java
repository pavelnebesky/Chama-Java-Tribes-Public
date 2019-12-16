package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserValid(User user) {
        return userRepository.findById(user.getId()).isPresent();
    }

    public Optional<User> findById(Long userId) {
        Optional user = userRepository.findById(userId);
        return user;
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }
}